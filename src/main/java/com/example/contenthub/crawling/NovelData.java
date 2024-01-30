package com.example.contenthub.crawling;

public class NovelData {
    private final String title;
    private final String coverImg;
    private final String summary;
    private final String genre;

    public NovelData(String title, String coverImg, String summary, String genre) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

}
