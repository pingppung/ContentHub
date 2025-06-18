package com.example.contenthub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDataDTO {
    private String title;
    private String synopsis;
    private String coverImg;
    private List<String> genre;
    private String ageRating;
    private String contentId;

    // 영화
    private List<String> actors;
    private String country;

    // 드라마
    private String releaseDate;
    private String status;

    // 웹툰 - 연재 요일
    private String publish;

    public ContentDataDTO(String title, String synopsis, String coverImg, List<String> genre, String ageRating,
            String contentId) {
        this.title = title;
        this.synopsis = synopsis;
        this.coverImg = coverImg;
        this.genre = genre;
        this.ageRating = ageRating;
        this.contentId = contentId;
    }
}
