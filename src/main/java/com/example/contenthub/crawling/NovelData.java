package com.example.contenthub.crawling;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "novel")
public class NovelData {
    @Id
    private final String title;
    private final String coverImg;
    private final String summary;
    private final String genre;
    private final Site site;

    public NovelData(String title, String coverImg, String summary, String genre, Site site) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.site = site;
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

    public Site getSite() {
        return site;
    }
}
