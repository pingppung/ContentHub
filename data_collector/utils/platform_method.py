from enum import Enum


class PlatformMethod(Enum):
    API = "API"
    CRAWLING = "CRAWLING"


PLATFORM_METHOD_BY_CATEGORY = {
    "naver": {
        "webtoon": PlatformMethod.API,
        "novel": PlatformMethod.CRAWLING,
    },
    "kakao": {
        "webtoon": PlatformMethod.CRAWLING,
        "novel": PlatformMethod.CRAWLING,
    },
    "wavve": {
        "movie": PlatformMethod.API,
        "drama": PlatformMethod.API,
    },
    "tving": {
        "drama": PlatformMethod.API,
    },
}
