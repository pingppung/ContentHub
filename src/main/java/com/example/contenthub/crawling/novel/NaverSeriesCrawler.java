package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NaverSeriesCrawler {
    final static String base_url = "https://series.naver.com";
    final static String top100 = "/novel/top100List.series?rankingTypeCode=HOURLY&categoryCode=ALL&page=";

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
            String title = extractTitle(novel.text());
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
                System.out.println("해당 작품 id를 찾을 수가 없습니다!");
            }
            boolean adultContent = false;

            Elements adultElements = element.select(".comic_cont h3 em[class*=ico n19]");
            if (!adultElements.isEmpty()) adultContent = true;
            NovelData novelData = new NovelData(title, coverImg, summary, genre, Arrays.asList(new SiteDTO(NovelSite.NAVER_SERIES.getName(), productId)), adultContent);

            novels.add(novelData);
        }
        return novels;
    }


    // 정규표현식을 사용하여 [ ... ] 패턴을 찾아내고 해당 부분을 제거
    private static String extractTitle(String title) {
        return title.replaceAll("\\[.*?\\]", "").trim();
    }
}
