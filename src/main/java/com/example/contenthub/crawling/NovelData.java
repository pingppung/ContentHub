package com.example.contenthub.crawling;

public class NovelData {
    private final String title;
    private final String coverImg;
    private final String summary;

    public NovelData(String title, String coverImg, String summary) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getSummary() {
        return summary;
    }


}
