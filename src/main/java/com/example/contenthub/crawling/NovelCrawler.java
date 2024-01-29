package com.example.contenthub.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NovelCrawler {
    final static String base_url = "https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=";

    public static List<NovelData> crawl() throws IOException {
        List<NovelData> novels = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            String url = base_url + i;
            Document doc = Jsoup.connect(url).get();

            List<NovelData> list = getDataList(doc);
            novels.addAll(list);
        }
        return novels;
    }

    private static List<NovelData> getDataList(Document document) {
        List<NovelData> novels = new ArrayList<>();
        Elements novelElements = document.select(".comic_top_lst li");

        for (Element element : novelElements) {
            String title = element.select(".comic_cont a[class*=NPI=a:content]").text();
            String coverImg = element.select("img").attr("src");
            String summary = element.select(".comic_cont p[class*=dsc]").text();
            NovelData novelData = new NovelData(title, coverImg, summary);
            novels.add(novelData);
        }
        return novels;
    }
}