package com.example.contenthub.service.content;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.contenthub.dto.ContentDataDTO;
import com.example.contenthub.utils.ContentDataUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataCollectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ContentService contentService;

    public void collectData(String platform, String category) throws IOException {
        System.out.println("데이터 수집 시작해용~");

        String pythonUrl = "http://localhost:5000/datahub/gather/content";

        // 1. 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 요청 바디 구성 (플랫폼, 카테고리 전달)
        Map<String, String> body = new HashMap<>();
        body.put("platform", platform);
        body.put("category", category);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // 3. POST 요청 보내기
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                pythonUrl,
                request,
                JsonNode.class);

        JsonNode responseBody = response.getBody();
        System.out.println("수집 결과: " + responseBody);
        JsonNode data = responseBody.get("data");
        List<ContentDataDTO> contentList = ContentDataUtils.convertToDTOList(data);
        contentService.saveContents(contentList, platform, category);

    }

}
