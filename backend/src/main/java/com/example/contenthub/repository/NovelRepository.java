package com.example.contenthub.repository;

import com.example.contenthub.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

    // 제목으로 Novel 조회
    Novel findByTitle(String title);

    // 제목에 특정 문자열이 포함된 Novel 조회
    List<Novel> findByTitleContaining(String title);

    // 장르로 Novel 조회
    List<Novel> findByGenre(String genre);

    // 모든 Novel 조회
    List<Novel> findAll();

    // 제목으로 Novel 삭제
    void deleteByTitle(String title);
}
