package com.example.contenthub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDataDTO {
    private String title;
    private String synopsis;
    private String coverImg;
    private List<String> genre;
    private String ageRating;
    private String contentId;
}
