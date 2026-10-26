import re
from datetime import datetime
from decimal import Decimal


DETAIL_FIELDS = {
    "NOVEL": {"authors": ("author",), "first_published_date": ("firstPublishedDate",)},
    "WEBTOON": {
        "writers": ("writer",), "artists": ("artist",),
        "first_published_date": ("firstPublishedDate",),
    },
    "MOVIE": {
        "countries": ("country",), "actors": (), "release_year": ("releaseYear",),
        "duration_minutes": ("durationMinutes",), "directors": ("director",),
        "release_date": (),
    },
    "DRAMA": {
        "release_year": ("releaseYear",), "actors": (), "season_count": ("seasonCount",),
        "broadcasters": ("broadcaster",), "broadcast_days": ("broadcastDays",),
        "total_episode_count": (), "first_aired_date": ("firstAiredDate",),
        "last_aired_date": ("lastAiredDate",),
    },
}

def normalize_collected_content(item: dict, content_type: str) -> dict:
    detail = item.get("detail") or {}

    def value(name, *aliases):
        for source in (item, detail):
            for key in (name, *aliases):
                if source.get(key) is not None:
                    return source[key]
        return None

    result = {
        "title": item.get("title"),
        "synopsis": item.get("synopsis"),
        "genre": item.get("genre"),
        "cover_img": value("cover_img", "coverImg"),
        "content_id": value("content_id", "id"),
        "age_rating": value("age_rating", "ageRating"),
        "status": value("status"),
        "release_days": value("release_days", "releaseDays", "publish_days"),
        "schedule_type": value("schedule_type", "scheduleType"),
        "episode_count": value("episode_count", "episodeCount"),
    }
    if not isinstance(result["title"], str) or not result["title"].strip():
        raise ValueError("Collected content is missing title")
    if result["content_id"] is None or not str(result["content_id"]).strip():
        raise ValueError("Collected content is missing content_id")
    result["content_id"] = str(result["content_id"])
    if result["age_rating"] is not None:
        result["age_rating"] = str(result["age_rating"])
    for field, aliases in DETAIL_FIELDS[content_type].items():
        result[field] = value(field, *aliases)
    result["title"] = result["title"].strip()
    result["genre"] = as_list(result["genre"])
    result["release_days"] = normalize_days(result["release_days"])
    result["status"] = normalize_status(value("status")) if content_type != "MOVIE" else "UNKNOWN"
    result["schedule_type"] = normalize_schedule(result["schedule_type"])
    result["episode_count"] = as_number(result["episode_count"], integer=True)

    integer_fields = (
        "release_year", "duration_minutes", "season_count", "total_episode_count"
    )
    date_fields = (
        "first_published_date", "release_date", "first_aired_date", "last_aired_date"
    )
    list_fields = (
        "authors", "writers", "artists", "actors", "directors", "countries", "broadcasters"
    )
    for name in integer_fields:
        if name in result:
            result[name] = as_number(result[name], integer=True)
    for name in date_fields:
        if name in result:
            result[name] = normalize_date(result[name])
    for name in list_fields:
        if name in result:
            result[name] = as_list(result[name])
    if "broadcast_days" in result:
        result["broadcast_days"] = normalize_days(result["broadcast_days"])

    extra_fields = {
        "publisher": value("publisher"),
        "platform_published_date": normalize_date(value("platform_published_date")),
        "availability_status": normalize_availability(value("availability_status")),
        "rating": as_number(value("rating")),
        "rating_max": as_number(value("rating_max")),
        "rating_count": as_number(value("rating_count"), integer=True),
    }
    rating = extra_fields["rating"]
    maximum = extra_fields["rating_max"]
    if rating is not None and (maximum is None or maximum <= 0 or rating > maximum):
        raise ValueError("평점과 평점 만점을 확인해주세요.")
    for name, item_value in {**detail, **item}.items():
        if name not in result and name not in ("detail", "id", "coverImg"):
            extra_fields.setdefault(name, item_value)
    result["extra_fields"] = {
        name: item_value for name, item_value in extra_fields.items()
        if item_value is not None
    }
    return result


def as_list(value):
    if value is None:
        return None
    if isinstance(value, str):
        return [value.strip()] if value.strip() else []
    if not isinstance(value, list) or any(not isinstance(item, str) for item in value):
        raise ValueError("문자열 또는 문자열 배열이 필요합니다.")
    return [item.strip() for item in value if item.strip()]


def as_number(value, integer=False):
    if value is None or value == "":
        return None
    if isinstance(value, bool):
        raise ValueError("숫자에 참/거짓 값을 사용할 수 없습니다.")
    text = str(value).replace(",", "").strip()
    pattern = r"(?:총\s*)?(\d+(?:\.\d+)?)\s*(?:화|회|개|명|분|년|점|시즌)?"
    match = re.fullmatch(pattern, text)
    if not match:
        raise ValueError(f"숫자 형식을 확인해주세요: {value}")
    number = Decimal(match.group(1))
    if integer and number != number.to_integral_value():
        raise ValueError("정수가 필요합니다.")
    return int(number) if integer else float(number)


def normalize_date(value):
    if value is None or value == "":
        return None
    text = str(value).strip()
    for date_format in ("%Y-%m-%d", "%Y.%m.%d", "%Y.%m.%d.", "%Y/%m/%d", "%Y년 %m월 %d일"):
        try:
            return datetime.strptime(text, date_format).date().isoformat()
        except ValueError:
            pass
    raise ValueError(f"날짜 형식을 확인해주세요: {value}")


def normalize_status(value):
    text = str(value or "").strip().upper().replace(" ", "")
    statuses = {
        "연재중": "ONGOING", "방영중": "ONGOING",
        "완결": "COMPLETED", "방영종료": "COMPLETED",
        "휴재": "HIATUS", "휴재중": "HIATUS",
        "공개예정": "UPCOMING", "연재예정": "UPCOMING",
    }
    if text in ("ONGOING", "COMPLETED", "HIATUS", "UPCOMING"):
        return text
    return statuses.get(text, "UNKNOWN")


def normalize_schedule(value):
    text = str(value or "").strip().upper()
    return {
        "REGULAR": "REGULAR", "정기": "REGULAR",
        "IRREGULAR": "IRREGULAR", "비정기": "IRREGULAR", "불규칙": "IRREGULAR",
    }.get(text, "UNKNOWN")


def normalize_days(value):
    if value is None:
        return None
    days = {
        "월": "MONDAY", "화": "TUESDAY", "수": "WEDNESDAY",
        "목": "THURSDAY", "금": "FRIDAY", "토": "SATURDAY", "일": "SUNDAY",
    }
    result = []
    for item in as_list(value):
        text = item.strip().upper()
        if text in days.values():
            matches = [text]
        elif text in ("매일", "매일 연재"):
            matches = list(days.values())
        elif re.fullmatch(r"(?:매주\s*)?[월화수목금토일](?:요일)?(?:[\s,/·]+[월화수목금토일](?:요일)?)*(?:\s*연재)?", text):
            matches = [days[day] for day in re.findall(r"[월화수목금토일](?=요일|[\s,/·]|$)", text.replace(" 연재", ""))]
        else:
            raise ValueError(f"요일 형식을 확인해주세요: {item}")
        for day in matches:
            if day not in result:
                result.append(day)
    return result


def normalize_availability(value):
    if value is None:
        return None
    text = str(value).strip().upper()
    if text in ("AVAILABLE", "UNAVAILABLE", "REMOVED", "UNKNOWN"):
        return text
    return {
        "이용가능": "AVAILABLE", "이용불가": "UNAVAILABLE", "서비스종료": "REMOVED",
    }.get(text.replace(" ", ""), "UNKNOWN")
