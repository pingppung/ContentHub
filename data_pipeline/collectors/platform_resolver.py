from data_pipeline.enums import Platform


PLATFORM_MAP = {
    "네이버시리즈": Platform.NAVER_SERIES,
    "네이버웹툰": Platform.NAVER_WEBTOON,
    "카카오웹툰": Platform.KAKAO_WEBTOON,
    "카카오페이지": Platform.KAKAO_PAGE,
    "웨이브": Platform.WAVVE,
    "티빙": Platform.TVING,
}


class PlatformResolver:

    @staticmethod
    def resolve(platform_key: str) -> Platform:
        if not platform_key:
            raise ValueError("플랫폼 값이 없습니다.")

        key = platform_key.strip()

        if key in PLATFORM_MAP:
            return PLATFORM_MAP[key]

        try:
            return Platform(key.upper())
        except ValueError:
            raise ValueError(
                f"지원하지 않는 플랫폼입니다: {platform_key}"
            )