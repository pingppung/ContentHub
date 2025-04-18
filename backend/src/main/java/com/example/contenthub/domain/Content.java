package com.example.contenthub.domain;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "contents")
public class Content {

    @Id
    private String id;

    private String title;
    private String description;
    private String genre;

    @Field("cover_image")
    private String coverImg;

    private String category;

    private List<SiteDetail> sites = new ArrayList<>();

    public Content(String title, String description, String genre, String coverImg, String category) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.coverImg = coverImg;
        this.category = category;
    }

    public void addSite(SiteDetail newSite) {
        boolean exists = sites.stream().anyMatch(site -> site.getPlatform().equals(newSite.getPlatform()) &&
                site.getContentID().equals(newSite.getContentID()));
        if (!exists) {
            sites.add(newSite);
        }
    }
}