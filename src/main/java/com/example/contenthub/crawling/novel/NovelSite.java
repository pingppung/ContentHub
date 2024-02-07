package com.example.contenthub.crawling.novel;

public enum NovelSite {
    NAVER_SERIES("네이버 시리즈", "https://series.naver.com"),
    RIDI_BOOKS("리디북스", "https://www.ridibooks.com");

    private final String name;
    private final String baseUrl;

    NovelSite(String name, String baseUrl) {
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
