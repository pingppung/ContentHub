package com.example.contenthub;

import com.example.contenthub.service.NovelCrawlerService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.contenthub.crawling.novel.NovelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
class HubController {
    private final NovelCrawlerService novelCrawlerService;

    @Autowired
    public HubController(NovelCrawlerService novelCrawlerService) {
        this.novelCrawlerService = novelCrawlerService;
    }

    @GetMapping("/novel")
    public List<NovelData> getNovelsByGenre(@RequestParam String genre) {
        if (genre.equals("전체")) return novelCrawlerService.getAllData();
        return novelCrawlerService.getDataByGenre(genre);

    }

    // 해당 위치에 들어갈때마다 크롤링으로 인한 속도 문제로 빠른 테스트 진행을 위해 잠깐 주석처리해놓음
    @GetMapping("/api/series")
    public void crawl() throws IOException {
        //novelCrawlerService.crawl();
    }

    @GetMapping("/novel/search")
    public List<NovelData> searchNovelsByTitle(@RequestParam String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return novelCrawlerService.getDataByTitleContaining(decodedTitle);
    }


}