package com.example.contenthub.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponseDTO {
    private String title; 
    private String description;    
    private String coverImg;   
    private String genre;      
    private List<LinkDTO> links;

    public ContentResponseDTO(String title, String description, String coverImg, String genre) {
        this.title = title;
        this.description = description;
        this.coverImg = coverImg;
        this.genre = genre;
        this.links = new ArrayList<>();
    }
}
