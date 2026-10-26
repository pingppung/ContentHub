package com.example.contenthub.recommendation;

import java.util.List;

import com.example.contenthub.content.dto.ContentDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecommendResponse {

    @JsonProperty("top_contents")
    private final List<ContentDto> topContents;

    public static RecommendResponse of(List<ContentDto> contents) {
        return new RecommendResponse(contents);
    }
}