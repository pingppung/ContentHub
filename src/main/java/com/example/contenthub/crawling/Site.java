package com.example.contenthub.crawling;

public class Site {
    private final String siteName;
    private final String id;

    public Site(String siteName, String id) {
        this.siteName = siteName;
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getId() {
        return id;
    }
}
