package com.example.contenthub.recommendation;

import com.example.contenthub.content.dto.ContentDto;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
class RecommendController {

    private final ContentRecommendService recommendService;

    @PostMapping("/similar")
    public ResponseEntity<?> recommend(@RequestBody RecommendRequest request) {

        List<ContentDto> recommendations = recommendService.getSimilarRecommendations(request);
        return ResponseEntity.ok(RecommendResponse.of(recommendations));
    }
}