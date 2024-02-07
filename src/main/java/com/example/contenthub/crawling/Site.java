package com.example.contenthub.crawling;

import org.springframework.data.mongodb.core.mapping.Field;

public class Site {
    @Field("site_name")
    private final String siteName;

    @Field("site_id")
    private final String id;

    public Site(String siteName, String id) {
        this.siteName = siteName;
        this.id = id;
    }
}
