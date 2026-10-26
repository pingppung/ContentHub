package com.example.contenthub.content;

import com.example.contenthub.content.enums.ContentType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.contenthub.content.detail.ContentDetail;
import com.example.contenthub.content.detail.SiteDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Document(collection = "contents")
public class Content {

    @Id
    private String id;

    // Representative metadata, refreshed from the latest successful source collection.
    // Platform-specific original variants are not retained in this model.
    private String title;
    private String synopsis;
    private List<String> genre;

    @Field("cover_image")
    private String coverImg;

    private ContentType type;
    
    private List<SiteDetail> sites = new ArrayList<>();

    @Field("collected_at")
    // Summary of the most recently recorded successful source collection.
    // Retained for list sorting; per-source timestamps live in SiteDetail.
    private Instant lastCollectedAt;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    private ContentDetail detail;

    public static Content of(
            String title,
            String synopsis,
            List<String> genre,
            String coverImg,
            ContentType type
    ) {
        Content content = new Content();
        validateTitle(title);
        content.title = title;
        content.synopsis = synopsis;
        content.genre = genre;
        content.coverImg = coverImg;
        content.type = Objects.requireNonNull(type, "type is required");
        content.createdAt = Instant.now();
        content.updatedAt = content.createdAt;

        return content;
    }

    public void addSite(SiteDetail newSite) {
        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(newSite, "site is required");
        Objects.requireNonNull(newSite.getPlatform(), "platform is required");
        if (newSite.getCid() == null || newSite.getCid().isBlank()) {
            throw new IllegalArgumentException("cid is required");
        }

        boolean exists = sites.stream()
                .anyMatch(site ->
                        site.getPlatform().equals(newSite.getPlatform()) &&
                        site.getCid().equals(newSite.getCid())
                );

        if (!exists) {
            sites.add(newSite);
        }
    }

    void initializeCollectedData(ContentDetail detail) {
        this.detail = detail;
        this.lastCollectedAt = Instant.now();
        this.updatedAt = this.lastCollectedAt;
    }

    static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
    }
}
