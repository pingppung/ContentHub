package com.example.contenthub.recommendation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.contenthub.content.Content;
import com.example.contenthub.content.dto.ContentDto;
import com.example.contenthub.content.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentRecommendService {

    private static final String FAST_API_URL = "http://localhost:5000/recommend/similar";

    private final ContentRepository contentRepository;
    private final RestTemplate restTemplate;

    public List<ContentDto> getSimilarRecommendations(RecommendRequest request) {

        ContentDto query = request.getQuery();

        // 추천 기준 콘텐츠 조회
        Content selected = contentRepository.findById(query.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));

        // 같은 카테고리의 추천 후보 조회
        List<Content> contents = contentRepository.findByType(request.getType());

        List<ContentDto> contentList = contents.stream()
                .filter(content -> !content.getId().equals(selected.getId()))
                .map(this::toDTO)
                .toList();

        ContentDto queryDTO = toDTO(selected);

        RecommendRequest requestToSend = new RecommendRequest(
                contentList,
                queryDTO,
                request.getType());

        ResponseEntity<RecommendResponse> response = restTemplate.postForEntity(
                FAST_API_URL,
                requestToSend,
                RecommendResponse.class);

        RecommendResponse responseBody = response.getBody();

        if (responseBody == null || responseBody.getTopContents() == null) {
            return List.of();
        }

        return responseBody.getTopContents();
    }

    private ContentDto toDTO(Content content) {
        return ContentDto.builder()
                .contentId(content.getId())
                .title(content.getTitle())
                .synopsis(content.getSynopsis())
                .genre(content.getGenre())
                .type(content.getType())
                .detail(content.getDetail())
                .coverImg(content.getCoverImg())
                .build();
    }

}
