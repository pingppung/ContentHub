package com.example.contenthub.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "likes")
public class Like {

    @Id
    private String id;

    private String userId;
    private List<String> contentIds;

    public Like(String userId, String contentId) {
        this.userId = userId;
        this.contentIds = new ArrayList<>();
        this.contentIds.add(contentId);
    }

    public void addContent(String contentId) {
        if (!this.contentIds.contains(contentId)) {
            this.contentIds.add(contentId);
        }
    }

}
