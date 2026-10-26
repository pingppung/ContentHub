package com.example.contenthub.content.collector;

import com.example.contenthub.content.enums.ContentType;
import com.example.contenthub.content.ContentService;
import com.example.contenthub.content.dto.ContentDto;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataCollectorService {

    private final RestTemplate restTemplate;
    private final ContentService contentService;

    @Async
    public void collectData(String platform, ContentType type) throws IOException {

        String pythonUrl = "http://localhost:5000/datahub/gather/content";

        // 1. 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 요청 바디 구성 (플랫폼, 타입 전달)
        Map<String, String> body = new HashMap<>();
        body.put("platform", platform);
        body.put("type", type.name());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // 3. POST 요청 보내기
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                pythonUrl,
                request,
                JsonNode.class);

        JsonNode responseBody = response.getBody();
        
        if (responseBody == null || responseBody.get("data") == null) {
            throw new IllegalStateException("수집 결과가 없습니다.");
        }

        JsonNode data = responseBody.get("data");

        List<ContentDto> contentList = ContentDataUtils.convertToDTOList(data, type);
        contentService.saveContents(contentList, platform, type);

    }

}
