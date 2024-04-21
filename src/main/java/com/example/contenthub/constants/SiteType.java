package com.example.contenthub.constants;

public enum SiteType {
    KAKAO_PAGE("KakaoPage", "https://page.kakao.com"),
    NAVER_SERIES("NaverSeries", "https://series.naver.com");

    private final String name;
    private final String baseUrl;

    SiteType(String name, String baseUrl) {
        this.name = name;
        this.baseUrl = baseUrl;
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
