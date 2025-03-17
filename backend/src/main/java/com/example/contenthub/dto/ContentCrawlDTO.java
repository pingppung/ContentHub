package com.example.contenthub.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentCrawlDTO {
    private String title; 
    private String description;    
    private String coverImg;   
    private String genre;
    private boolean isAdultContent;       
    private String contentId;
}
