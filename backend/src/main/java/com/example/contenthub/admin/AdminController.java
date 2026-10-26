package com.example.contenthub.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.contenthub.content.dto.request.ContentCollectRequest;
import com.example.contenthub.content.ContentStatsService;
import com.example.contenthub.content.collector.DataCollectorService;
import com.example.contenthub.content.dto.response.StatisticsResponse;

import lombok.RequiredArgsConstructor;

// 관리자만 사용하는 크롤링,통계 전용
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final DataCollectorService dataCollectorService;
    private final ContentStatsService statsService;

    // 기존 @Scheduled 메서드
    // @GetMapping("/api/v1/collect")
    // @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    // public void crawlScheduled() throws IOException {
    // crawlerService.crawl();
    // }

    @PostMapping("/collect")
    public ResponseEntity<?> collectConetent(@RequestBody ContentCollectRequest request) throws IOException {
        try {
            dataCollectorService.collectData(request.getPlatform(), request.getType());
            return ResponseEntity.ok("데이터 수집 요청이 성공적으로 완료되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("데이터 수집 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> getStatistics() {
        // categoryCounts 통계 가져오기
        List<StatisticsResponse.FieldCount> categoryCounts = statsService.getContentTypeCount();
        List<StatisticsResponse.DailyCount> userGrowth = statsService.getUserGrowth();
        // List<StatisticsResponse.TitleViews> popularTitles =
        // statsService.getPopularTitles();
        // List<StatisticsResponse.DailyCount> dailyVisits =
        // statsService.getDailyVisits();

        StatisticsResponse response = new StatisticsResponse(
                categoryCounts,
                userGrowth);
        // popularTitles,
        // dailyVisits

        return ResponseEntity.ok(response);
    }

}
