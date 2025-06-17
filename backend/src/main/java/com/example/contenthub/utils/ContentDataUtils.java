package com.example.contenthub.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.contenthub.dto.ContentDataDTO;
import com.fasterxml.jackson.databind.JsonNode;

public class ContentDataUtils {

    public static List<ContentDataDTO> convertToDTOList(JsonNode nodeList) {
        List<ContentDataDTO> contentList = new ArrayList<>();

        if (nodeList != null) {
            for (JsonNode node : nodeList) {
                ContentDataDTO content = new ContentDataDTO(
                        node.has("title") ? node.get("title").asText() : "",
                        node.has("synopsis") ? node.get("synopsis").asText() : "",
                        node.has("cover_img") ? node.get("cover_img").asText() : "",
                        convertGenre(node.get("genre")),
                        node.has("age_rating") ? node.get("age_rating").asText() : "12",
                        node.has("content_id") ? node.get("content_id").asText() : "");
                contentList.add(content);
            }
        }
        return contentList;
    }

    private static List<String> convertGenre(JsonNode genreNode) {
        List<String> genreList = new ArrayList<>();
        for (JsonNode g : genreNode) {
            genreList.add(g.asText());
        }
        return genreList;
    }
}