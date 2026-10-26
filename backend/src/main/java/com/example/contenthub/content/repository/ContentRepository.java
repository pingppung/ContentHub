package com.example.contenthub.content.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.contenthub.content.Content;
import com.example.contenthub.content.enums.ContentType;
import com.example.contenthub.content.enums.Platform;

public interface ContentRepository extends MongoRepository<Content, String> {

    @Query("{ 'type': ?0, 'sites': { '$elemMatch': { 'platform': ?1, 'cid': ?2 } } }")
    Optional<Content> findBySource(ContentType type, Platform platform, String cid);

    List<Content> findByType(ContentType type);
}
