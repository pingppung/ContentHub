package com.example.contenthub.dto;

import com.example.contenthub.constants.SiteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkDTO {
    private String siteName;
    private String contentId;
    private String url;

    public LinkDTO(SiteType siteType, String contentId) {
        this.siteName = siteType.getName();
        this.url = siteType.getBaseUrl() + "/" + contentId;
        this.contentId = contentId;
    }

    public LinkDTO(String siteName, String uid, String url) {
        this.siteName = siteName;
        this.contentId = uid;
        this.url = url;
    }
}

