package com.example.contenthub.controller;

import com.example.contenthub.dto.CollectRequestDTO;
import com.example.contenthub.dto.ContentResponseDTO;
import com.example.contenthub.service.content.ContentService;
import com.example.contenthub.service.content.DataCollectorService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class DataCollectorController {
    private final ContentService crawlerService;
    private final DataCollectorService dataCollectorService;

    @GetMapping("/contents/search")
    public List<ContentResponseDTO> getContents(
            @RequestParam(value = "category") String category,
            @RequestParam(value = "genre") String genre,
            @RequestParam(value = "title", required = false) String title) {
        System.out.println(category + " " + genre + " " + title);
        return crawlerService.filterContents(genre, title, category);
    }

    // 기존 @Scheduled 메서드
    // @GetMapping("/api/v1/collect")
    // @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    // public void crawlScheduled() throws IOException {
    // crawlerService.crawl();
    // }

    // 관리자용 데이터 수집 버튼
    // 파라미터 값 - 플랫폼, 카테고리
    @PostMapping("/api/v1/collect")
    public ResponseEntity<?> collectConetent(@RequestBody CollectRequestDTO request) throws IOException {
        try {
            System.out.println(request.getPlatforms() + " " + request.getCategories());
            dataCollectorService.collectData(request.getPlatforms().get(0), request.getCategories().get(0));
            return ResponseEntity.ok("데이터 수집 요청이 성공적으로 완료되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("데이터 수집 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }
    // @GetMapping("/api/novel/search")
    // public List<ContentDTO> searchNovelsByTitle(@RequestParam("title") String
    // title) {
    // String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
    // return crawlerService.getDataByTitleContaining(decodedTitle);
    // }

}