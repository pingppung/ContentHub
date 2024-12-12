package com.example.contenthub.controller;

import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.service.crawling.NovelCrawlerService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class CrawlerController {
    private final NovelCrawlerService novelCrawlerService;

    @GetMapping("/api/novel")
    public List<ContentDTO> getNovelsByGenre(@RequestParam(value = "genre") String genre) {
        System.out.println(genre);
        if (genre.equals("전체")) {
            return novelCrawlerService.getAllData();
        }
        return novelCrawlerService.getDataByGenre(genre);
    }

    // 기존 @Scheduled 메서드
    @GetMapping("/api/crawler")
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public void crawlScheduled() throws IOException {
        novelCrawlerService.crawl();
    }

    // 관리자용 크롤링 버튼
    @PostMapping("/api/crawler")
    public ResponseEntity<?> crawl() throws IOException {
        try {
            novelCrawlerService.crawl();
            return ResponseEntity.ok("크롤링이 성공적으로 완료되었습니다!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("크롤링 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/api/novel/search")
    public List<ContentDTO> searchNovelsByTitle(@RequestParam("title") String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return novelCrawlerService.getDataByTitleContaining(decodedTitle);
    }

}