package com.example.contenthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.Like;
import com.example.contenthub.domain.User;

public interface LikeRepository extends JpaRepository<Like, Integer> {
}
