from enum import Enum


class Platform(Enum):
    NAVER_SERIES = "NAVER_SERIES"
    NAVER_WEBTOON = "NAVER_WEBTOON"
    KAKAO_PAGE = "KAKAO_PAGE"
    KAKAO_WEBTOON = "KAKAO_WEBTOON"
    WAVVE = "WAVVE"
    TVING = "TVING"


class ContentType(Enum):
    MOVIE = "movie"
    DRAMA = "drama"
    WEBTOON = "webtoon"
    NOVEL = "novel"


class CollectionMethod(Enum):
    API = "API"
    CRAWLING = "CRAWLING"
