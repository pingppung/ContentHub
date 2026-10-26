package com.example.contenthub.content.dto;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.response.ContentLinkResponse;

import java.util.List;

import com.example.contenthub.content.detail.ContentDetail;
import com.example.contenthub.content.detail.SiteDetail;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// Shared transfer model for collection, content queries, and recommendations.
public class ContentDto {

    private String title;
    private String synopsis;
    private String coverImg;
    private List<String> genre;

    private String ageRating;
    // External platform ID during collection; internal Content.id in query responses.
    private String contentId;

    private ContentType type;

    private ContentDetail detail;
    // Publication metadata from the platform currently being collected.
    private SiteDetail publicationInfo;

    private List<ContentLinkResponse> links;

}
