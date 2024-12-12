package com.example.contenthub.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@MappedSuperclass
@SuperBuilder
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long contentId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "adult_content", nullable = false)
    private boolean adultContent;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "content_type", nullable = false)
//    private ContentType contentType;

    @Column(name = "genre", nullable = false)
    private String genre;


    public Content(String title, String summary, String coverImg, boolean adultContent, String genre) {
        this.title = title;
        this.description = summary;  // description을 summary로 설정
        this.imageUrl = coverImg;    // coverImg를 imageUrl로 설정
        this.adultContent = adultContent;
        this.genre = genre;
    }

}
