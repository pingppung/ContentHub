package com.example.contenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.contenthub.entity.Content;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    Content findByTitleAndCategory(String title, String category);

    // 카테고리로 조회
    List<Content> findByCategory(String category);

    // 카테고리 + 장르 (장르는 선택, 기본값: "전체")
    List<Content> findByCategoryAndGenre(String category, String genre);

    List<Content> findByCategoryAndTitle(String category, String title);

    // 카테고리 + 제목 검색 (제목 일부 포함)
    List<Content> findByCategoryAndTitleContaining(String category, String title);

    // 카테고리 + 장르 + 제목 검색
    List<Content> findByCategoryAndGenreAndTitleContaining(String category, String genre, String title);

}
