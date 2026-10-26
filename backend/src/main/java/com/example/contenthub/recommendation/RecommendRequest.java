package com.example.contenthub.recommendation;

import java.util.List;

import com.example.contenthub.content.dto.ContentDto;
import com.example.contenthub.content.enums.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRequest {
    private List<ContentDto> contentList;
    private ContentDto query;
    private ContentType type;
}
