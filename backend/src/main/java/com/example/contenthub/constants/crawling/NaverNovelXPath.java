package com.example.contenthub.constants.crawling;

public enum NaverNovelXPath {
    URL("https://series.naver.com/novel/specialFreeList.series?specialFreeTypeCode=HOURLYFREE&page="),
    DETAIL_URL("https://series.naver.com/novel/detail.series?productNo="),
    LIST("//*[@id=\"content\"]/div/div/ul/li"),
    TITLE("//*[@id=\"content\"]/div[1]/h2"),//*[@id="content"]/div[1]/h2/text()
    COVER_IMG("//*[@id=\"container\"]/div[1]//img"),
    DESCRIPTION_WITH_MORE("//*[@id=\"content\"]/div[2]/div[2]"),
    DESCRIPTION_NO_MORE("//*[@id=\"content\"]/div[2]/div"),
    GENRE("//*[@id=\"content\"]/ul[1]/li/ul/li[2]/span/a"),
    IS_ADULT("//*[@id=\"content\"]/div[1]/h2/span"),
    PAGE_COUNT("//p[@class='pagenate']");

    private final String xpath;

    NaverNovelXPath(String xpath) {
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }
}
