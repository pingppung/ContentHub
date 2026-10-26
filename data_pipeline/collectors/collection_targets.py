from data_pipeline.collectors.logins.kakao_login import activate_bot as kakao_login
from data_pipeline.collectors.logins.naver_login import activate_bot as naver_login
from data_pipeline.collectors.settings.kakao_page_selectors import SELECTORS as KAKAO_PAGE_SELECTORS
from data_pipeline.collectors.settings.naver_webtoon_api import API_SETTINGS as NAVER_WEBTOON_API_SETTINGS
from data_pipeline.collectors.settings.naver_series_selectors import SELECTORS as NAVER_SERIES_SELECTORS
from data_pipeline.collectors.settings.wavve_api import API_SETTINGS as WAVVE_API_SETTINGS
from data_pipeline.enums import ContentType, Platform, CollectionMethod


COLLECTION_TARGETS = {
    (Platform.KAKAO_PAGE, ContentType.NOVEL): {
        "method": CollectionMethod.CRAWLING,
        "settings": {
            **KAKAO_PAGE_SELECTORS["novel"],
            "login": {"enabled": False, "handler": kakao_login},
        },
    },
    (Platform.NAVER_SERIES, ContentType.NOVEL): {
        "method": CollectionMethod.CRAWLING,
        "settings": {
            **NAVER_SERIES_SELECTORS["novel"],
            "login": {"enabled": False, "handler": naver_login},
        },
    },
    (Platform.NAVER_WEBTOON, ContentType.WEBTOON): {
        "method": CollectionMethod.API,
        "settings": NAVER_WEBTOON_API_SETTINGS["webtoon"],
    },
    (Platform.WAVVE, ContentType.MOVIE): {
        "method": CollectionMethod.API,
        "settings": WAVVE_API_SETTINGS["movie"],
    },
    (Platform.WAVVE, ContentType.DRAMA): {
        "method": CollectionMethod.API,
        "settings": WAVVE_API_SETTINGS["drama"],
    },
}


def get_collection_target(platform: Platform, content_type: ContentType) -> dict:
    target = COLLECTION_TARGETS.get((platform, content_type))

    if target is None:
        raise ValueError(
            f"지원하지 않는 사이트/콘텐츠 조합입니다: {platform.value} / {content_type.value}"
        )

    return target
