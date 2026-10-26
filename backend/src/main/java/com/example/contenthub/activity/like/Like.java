package com.example.contenthub.activity.like;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    private String id;

    private String userId;

    private String contentId;

    @Builder
    public Like(String userId, String contentId) {
        this.userId = userId;
        this.contentId = contentId;
    }
}
