package com.example.contenthub.crawling;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NaverSeriesCrawler {
    final static String base_url = "https://series.naver.com";
    final static String top100 = "/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=";

    public List<NovelData> crawl() throws IOException {
        List<NovelData> novels = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            String url = base_url + top100 + i;
            Document doc = Jsoup.connect(url).get();

            List<NovelData> list = getNaverSeriesData(doc);
            novels.addAll(list);
        }
        return novels;
    }

    private List<NovelData> getNaverSeriesData(Document document) throws IOException {
        List<NovelData> novels = new ArrayList<>();
        Elements novelElements = document.select(".comic_top_lst li");

        for (Element element : novelElements) {
            Elements novel = element.select(".comic_cont a[class*=NPI=a:content]");
            String title = novel.text();
            String detailURL = base_url + novel.attr("href");
            Pattern pattern = Pattern.compile("productNo=(\\d+)");
            Matcher matcher = pattern.matcher(detailURL);

            Document doc = Jsoup.connect(detailURL).get();
            String genre = doc.select("li span a[href*=categoryTypeCode=genre]").text();

            String coverImg = element.select("img").attr("src");
            String summary = element.select(".comic_cont p[class*=dsc]").text();

            String productId = null;
            if (matcher.find()) {
                productId = matcher.group(1);
            } else {
                System.out.println("Product ID not found in URL");
            }
            NovelData novelData = new NovelData(title, coverImg, summary, genre, new Site("네이버시리즈", productId));
            novels.add(novelData);
        }
        return novels;
    }

}
