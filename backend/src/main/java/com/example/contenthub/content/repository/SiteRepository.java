package com.example.contenthub.content.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contenthub.content.enums.ContentType;
import com.example.contenthub.content.enums.Platform;
import com.example.contenthub.content.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {

    Optional<Site> findByPlatformAndContentTypeAndEnabledTrue(
            Platform platform,
            ContentType contentType
    );

    Optional<Site> findByPlatformAndContentType(
            Platform platform,
            ContentType contentType
    );
}