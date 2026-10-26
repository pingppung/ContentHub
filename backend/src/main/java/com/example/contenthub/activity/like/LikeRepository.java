package com.example.contenthub.activity.like;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {

    boolean existsByUserIdAndContentId(String userId, String contentId);

    Optional<Like> findByUserIdAndContentId(String userId, String contentId);

    List<Like> findAllByUserId(String userId);

    long countByContentId(String contentId);
}