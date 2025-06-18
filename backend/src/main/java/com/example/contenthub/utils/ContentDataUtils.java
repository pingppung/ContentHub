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
                        convertList(node.get("genre")),
                        node.has("age_rating") ? node.get("age_rating").asText() : "12",
                        node.has("content_id") ? node.get("content_id").asText() : "");
                // ✅ 선택적 필드도 있으면 저장
                if (node.has("actors")) {
                    content.setActors(convertList(node.get("actors")));
                }
                if (node.has("country")) {
                    content.setCountry(node.get("country").asText());
                }
                if (node.has("release_date")) {
                    content.setReleaseDate(node.get("release_date").asText());
                }
                if (node.has("publish_date")) {
                    content.setPublish(node.get("publish_date").asText());
                }
                if (node.has("status")) {
                    content.setStatus(node.get("status").asText());
                }
                contentList.add(content);
            }

        }
        return contentList;
    }

    private static List<String> convertList(JsonNode node) {
        List<String> list = new ArrayList<>();
        for (JsonNode g : node) {
            list.add(g.asText());
        }
        return list;
    }

}