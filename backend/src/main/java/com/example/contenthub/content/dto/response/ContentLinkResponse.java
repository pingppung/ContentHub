package com.example.contenthub.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentLinkResponse {

    private String platform;

    private String url;

    private String ageRating;
}