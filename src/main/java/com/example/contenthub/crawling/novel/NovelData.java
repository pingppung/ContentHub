package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.Site;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;


@Document(collection = "novel")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NovelData {
    @Id
    private final String title;
    private final String coverImg;
    private final String summary;
    private final String genre;

    @JsonIgnore
    private List<Site> site;

    private boolean adultContent;

    public NovelData(String title, String coverImg, String summary, String genre, List<Site> site, boolean adultContent) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.site = site;
        this.adultContent = adultContent;
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

    public List<Site> getSite() {
        return site;
    }

    public void setSite(List<Site> site) {
        this.site = site;
    }

    public boolean isAdultContent() {
        return adultContent;
    }
}
