package com.example.contenthub.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlingTest {
    final static String base_url = "https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=";
    public static List<String> crawl() throws IOException {
        List<String> novels = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            String url = base_url + i;
            Document doc = Jsoup.connect(url).get();

            List<String> list = getDataList(doc);
            novels.addAll(list);
        }
        return novels;
    }

    private static List<String> getDataList(Document document) {
        List<String> list = new ArrayList<>();
        Elements selects = document.select(".comic_cont a[class*=NPI=a:content]");

        for (Element select : selects) {
            list.add(select.html());
        }
        return list;
    }
}