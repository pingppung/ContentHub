package com.example.contenthub.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.contenthub.dto.ContentCrawlDTO;
import com.fasterxml.jackson.databind.JsonNode;

public class ContentCrawlUtils {

    public static List<ContentCrawlDTO> convertToDTOList(JsonNode nodeList) {
        List<ContentCrawlDTO> contentList = new ArrayList<>();
        
        if (nodeList != null) {
            for (JsonNode node : nodeList) {
                ContentCrawlDTO content = new ContentCrawlDTO(
                    node.has("title") ? node.get("title").asText() : "",
                    node.has("description") ? node.get("description").asText() : "",
                    node.has("cover_img") ? node.get("cover_img").asText() : "",
                    node.has("genre") ? node.get("genre").asText() : "",
                    node.has("is_adult_content") ? node.get("is_adult_content").asBoolean() : false,
                    node.has("content_id") ? node.get("content_id").asText() : ""
                );
                contentList.add(content);
            }
        }
        return contentList;
    }
}