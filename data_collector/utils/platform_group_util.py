from enum import Enum


class PlatformGroup(Enum):
    NAVER = "naver"
    KAKAO = "kakao"
    WAVVE = "wavve"
    TVING = "tving"


KOREAN_TO_PLATFORM_GROUP = {
    "네이버시리즈": PlatformGroup.NAVER,
    "네이버웹툰": PlatformGroup.NAVER,
    "카카오웹툰": PlatformGroup.KAKAO,
    "카카오페이지": PlatformGroup.KAKAO,
    "웨이브": PlatformGroup.WAVVE,
    "티빙": PlatformGroup.TVING,
}


class PlatformGroupUtil:
    @staticmethod
    def get_platform_group(platform_key: str) -> PlatformGroup:
        key = platform_key.lower()
        if key in KOREAN_TO_PLATFORM_GROUP:
            return KOREAN_TO_PLATFORM_GROUP[key]
        try:
            return PlatformGroup(key)
        except ValueError:
            raise ValueError(f"지원하지 않는 플랫폼입니다: {platform_key}")
