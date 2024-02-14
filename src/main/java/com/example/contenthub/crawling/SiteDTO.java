package com.example.contenthub.crawling;

import org.springframework.data.mongodb.core.mapping.Field;


public class SiteDTO {
    @Field("site_name")
    private final String siteName;

    @Field("site_id")
    private final String id;

    public SiteDTO(String siteName, String id) {
        this.siteName = siteName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSiteName() {
        return siteName;
    }
}
