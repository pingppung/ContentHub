package com.example.contenthub.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SiteDetail {
    private String platform;
    private String contentID;
    private boolean isAdult;
}
