package com.example.contenthub.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.contenthub.crawling.NaverSeriesCrawler;
import com.example.contenthub.crawling.NovelData;
import com.example.contenthub.crawling.Site;
import com.example.contenthub.repository.NovelRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NovelCrawlerService {

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private NaverSeriesCrawler naverSeriesCrawler;

    public void crawl() throws IOException {
        List<NovelData> naverSeriesNovels = naverSeriesCrawler.crawl();
        novelRepository.saveAll(naverSeriesNovels);
    }

    public List<NovelData> getAllData() {

        return novelRepository.findAll();
    }

    public List<NovelData> getDataByGenre(String genre) {
        return novelRepository.findByGenre(genre);
    }
}