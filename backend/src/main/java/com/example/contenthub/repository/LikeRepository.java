package com.example.contenthub.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.contenthub.domain.Like;

public interface LikeRepository extends MongoRepository<Like, String> {

    boolean existsByUserIdAndContentIds(String userId, String contentId);
    Like findByUserIdAndContentIds(String userId, String contentId);
    Like findByUserId(String userId);
}
