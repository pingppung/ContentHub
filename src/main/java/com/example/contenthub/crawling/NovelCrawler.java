package com.example.contenthub.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NovelCrawler {
    final static String base_url = "https://series.naver.com";
    final static String top100 = "/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=";

    public static List<NovelData> crawl() throws IOException {
        List<NovelData> novels = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            String url = base_url + top100 + i;
            Document doc = Jsoup.connect(url).get();

            List<NovelData> list = getDataList(doc);
            novels.addAll(list);
        }
        return novels;
    }

    private static List<NovelData> getDataList(Document document) throws IOException {
        List<NovelData> novels = new ArrayList<>();
        Elements novelElements = document.select(".comic_top_lst li");

        for (Element element : novelElements) {
            Elements novel = element.select(".comic_cont a[class*=NPI=a:content]");
            String title = novel.text();
            String detailURL = base_url + novel.attr("href");

            Document doc = Jsoup.connect(detailURL).get();
            String genre = doc.select("li span a[href*=categoryTypeCode=genre]").text();

            String coverImg = element.select("img").attr("src");
            String summary = element.select(".comic_cont p[class*=dsc]").text();

            NovelData novelData = new NovelData(title, coverImg, summary, genre);
            novels.add(novelData);
        }
        return novels;
    }
}