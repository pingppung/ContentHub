package com.example.contenthub.controller;

import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.service.NovelCrawlerService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class CrawlerController {
    private final NovelCrawlerService novelCrawlerService;

    @GetMapping("/api/novel")
    public List<ContentDTO> getNovelsByGenre(@RequestParam("genre") String genre) {
        if (genre.equals("전체")) {
            return novelCrawlerService.getAllData();
        }
        return novelCrawlerService.getDataByGenre(genre);
    }

    @GetMapping("/api/crawler")
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public void crawl() throws IOException {
        novelCrawlerService.crawl();
    }

    @GetMapping("/api/novel/search")
    public List<ContentDTO> searchNovelsByTitle(@RequestParam("title") String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return novelCrawlerService.getDataByTitleContaining(decodedTitle);
    }

}