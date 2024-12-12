package com.example.contenthub.dto;

import com.example.contenthub.constants.SiteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ContentDTO {
    private String title;         // 콘텐츠 제목
    private String coverImg;     // 커버 이미지
    private String summary;       // 요약
    private String genre;         // 장르
    private boolean adultContent; // 성인 콘텐츠 여부
    private List<LinkDTO> links;  // 링크 목록 (서버->클라 용)
    private LinkDTO link; //링크(서버->서버 용)

    // 크롤링할 때 사용
    public ContentDTO(String title, String coverImg, String summary, String genre, boolean adultContent, SiteType siteType, String contentId) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.adultContent = adultContent;
        this.link = new LinkDTO(siteType, contentId);
    }

    // 프론트로 데이터 넘겨줄 때 사용
    public ContentDTO(String title, String coverImg, String summary, String genre, boolean adultContent, List<LinkDTO> links) {
        this.title = title;
        this.coverImg = coverImg;
        this.summary = summary;
        this.genre = genre;
        this.adultContent = adultContent;
        this.links = links;
    }
}
