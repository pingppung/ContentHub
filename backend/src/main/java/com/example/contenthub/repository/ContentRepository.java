package com.example.contenthub.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.contenthub.domain.Content;

import java.util.List;

@Repository
public interface ContentRepository extends MongoRepository<Content, String> {

    Content findByCategoryAndTitle(String category, String title);

    // 카테고리로 조회
    List<Content> findByCategory(String category);

    // 카테고리 + 장르 (장르는 선택, 기본값: "전체")
    List<Content> findByCategoryAndGenre(String category, String genre);

    // 카테고리 + 제목 검색 (제목 일부 포함)
    List<Content> findByCategoryAndTitleContaining(String category, String title);

    // 카테고리 + 장르 + 제목 검색
    List<Content> findByCategoryAndGenreAndTitleContaining(String category, String genre, String title);

}
