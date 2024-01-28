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
    public void crawl() throws IOException {
        List<String> totalList = new ArrayList<>(); // 전체 데이터를 저장할 리스트

        for (int i = 1; i < 6; i++) {
            String url = base_url + i;
            Document doc = Jsoup.connect(url).get();

            List<String> list = getDataList(doc);
            totalList.addAll(list); // 현재 페이지의 데이터를 전체 리스트에 추가
        }

        // 전체 데이터 출력
        for (String data : totalList) {
            System.out.println(data);
        }
    }

    private List<String> getDataList(Document document) {
        List<String> list = new ArrayList<>();
        Elements selects = document.select(".comic_cont a[class*=NPI=a:content]");

        for (Element select : selects) {
            list.add(select.html()); // 데이터를 리스트에 추가
        }
        return list;
    }
}