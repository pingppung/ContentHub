package com.example.contenthub.controller;

import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.service.NovelCrawlerService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.contenthub.entity.Novel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
class CrawlerController {
    private final NovelCrawlerService novelCrawlerService;

    @Autowired
    public CrawlerController(NovelCrawlerService novelCrawlerService) {
        this.novelCrawlerService = novelCrawlerService;
    }

    @GetMapping("/api/novel")
    public List<ContentDTO> getNovelsByGenre(@RequestParam("genre") String genre) {
        if (genre.equals("전체")) {
            return novelCrawlerService.getAllData();
        }
        return novelCrawlerService.getDataByGenre(genre);
    }

    // 해당 위치에 들어갈때마다 크롤링으로 인한 속도 문제로 빠른 테스트 진행을 위해 잠깐 주석처리해놓음
    @GetMapping("/api/crawler")
    public void crawl() throws IOException {
        novelCrawlerService.crawl();
    }

    @GetMapping("/api/novel/search")
    public List<ContentDTO> searchNovelsByTitle(@RequestParam("title") String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return novelCrawlerService.getDataByTitleContaining(decodedTitle);
    }


}