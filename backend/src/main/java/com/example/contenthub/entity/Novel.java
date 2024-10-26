package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


//@Document(collection = "novel")
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Getter
@NoArgsConstructor
@Table(name = "novel")
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long novel_id;

    @JoinColumn(name = "title")
    private String title;

    @Column(name = "coverImg", columnDefinition = "varchar(1000)")
    private String coverImg;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;
    @Column(name = "genre")
    private String genre;

    @Column(name = "adultContent")
    private boolean adultContent;

    @OneToMany(mappedBy = "novel", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<NovelSite> sites = new ArrayList<>();

    public Novel(String title, String coverImg, String summary, String genre, boolean adultContent) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.adultContent = adultContent;
    }


    public boolean hasSite(String siteName) {
        return this.sites.stream()
                .anyMatch(site -> site.getSite().getName().equals(siteName));
    }
}
