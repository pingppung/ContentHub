package com.example.contenthub.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContentDTO {
    private String title;
    private String coverImg;
    private String summary;
    private String genre;
    private boolean adultContent;
    private String productId;
    private List<SiteDTO> siteDTOs;

    //크롤링할 때 사용
    public ContentDTO(String title, String coverImg, String summary, String genre, boolean adultContent, String productId) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.adultContent = adultContent;
        this.productId = productId;
    }

    //프론트로 데이터 넘겨줄 때 사용
    public ContentDTO(String title, String coverImg, String summary, String genre, boolean adultContent, List<SiteDTO> siteDTOs) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.adultContent = adultContent;
        this.siteDTOs = siteDTOs;
    }
}
