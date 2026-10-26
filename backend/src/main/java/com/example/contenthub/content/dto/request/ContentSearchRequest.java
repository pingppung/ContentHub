package com.example.contenthub.content.dto.request;

import com.example.contenthub.content.enums.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentSearchRequest {

    private ContentType type;
    private String genre;
    private String title;
}
