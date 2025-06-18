package com.example.contenthub.domain;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "contents")
public class Content {

    @Id
    private String id;

    private String title;
    private String synopsis;
    private List<String> genre;

    @Field("cover_image")
    private String coverImg;

    private String category;

    private List<SiteDetail> sites = new ArrayList<>();

    // 영화
    private String country;
    private List<String> actors;
    // 드라마
    private String releaseDate;
    private String status;
    // 웹툰
    private String publish;

    public Content(String title, String synopsis, List<String> genre, String coverImg, String category) {
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.coverImg = coverImg;
        this.category = category;
    }

    public void addSite(SiteDetail newSite) {
        boolean exists = sites.stream().anyMatch(site -> site.getPlatform().equals(newSite.getPlatform()) &&
                site.getCid().equals(newSite.getCid()));
        if (!exists) {
            sites.add(newSite);
        }
    }
}