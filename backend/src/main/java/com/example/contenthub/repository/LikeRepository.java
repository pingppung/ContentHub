package com.example.contenthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contenthub.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findByUserId(Long userId);
    List<Like> findByContentId(Long contentId);
    
}
