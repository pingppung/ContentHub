package com.example.contenthub.content.enums;

public enum Platform {
    KAKAO_PAGE("KakaoPage"),
    NAVER_SERIES("NaverSeries"),
    NAVER_WEBTOON("NaverWebtoon"),
    WAVVE("Wavve");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
