package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;


@Document(collection = "novel")
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NovelData {
    @Id
    private final String title;
    private final String coverImg;
    private final String summary;
    private final String genre;

    //@JsonIgnore
    @JsonSerialize
    private List<SiteDTO> site;

    private final boolean adultContent;

    public NovelData(String title, String coverImg, String summary, String genre, List<SiteDTO> site, boolean adultContent) {
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

    public List<SiteDTO> getSite() {
        return site;
    }

    public void setSite(List<SiteDTO> site) {
        this.site = site;
    }

    public boolean isAdultContent() {
        return adultContent;
    }
}
