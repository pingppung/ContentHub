package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private String genre;

    @Column(name = "cover_image")
    private String coverImg;

    private String category;
    
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ContentSite> sites = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Content(String title, String description, String genre, String coverImg, String category) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.coverImg = coverImg;
        this.category = category;
    }

    public void addContentSite(String contentID, boolean isAdultContent, Site site) {
        ContentSite contentSite = new ContentSite(contentID, isAdultContent, this, site);
        sites.add(contentSite);
    }
}