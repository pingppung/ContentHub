package com.example.contenthub.constants.crawling;

public enum KakaoNovelXPath {
    URL("https://page.kakao.com/menu/10011/screen/94"),
    DETAIL_URL("https://page.kakao.com/content/"),
    LIST("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div"),
    TITLE("//*[@id=\"__next\"]/div/div[2]/div[1]/div/div[1]/div[1]/div/div[2]/a/div/span[1]"),
    COVER_IMG("//*[@id=\"__next\"]//img[@alt='썸네일']"),
    //*[@id="__next"]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span
    //*[@id="__next"]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span
    //*[@id="__next"]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span
    //*[@id="__next"]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div[1]/span
    DESCRIPTION("//*[@id=\"__next\"]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div//span"),
    GENRE("//*[@id=\"__next\"]/div/div[2]/div[1]/div/div[1]/div[1]/div/div[2]/a/div/div[1]/div[1]/div/span[2]"),
    IS_ADULT(".//img[@alt='19세 뱃지']");

    private final String xpath;

    KakaoNovelXPath(String xpath) {
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }

    public static String buildUrl(String contentId) {
        return DETAIL_URL.xpath + contentId + "?tab_type=about";
    }
}
