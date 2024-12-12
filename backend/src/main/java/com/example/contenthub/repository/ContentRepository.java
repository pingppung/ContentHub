package com.example.contenthub.repository;

import com.example.contenthub.entity.Content;
import com.example.contenthub.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

    List<Content> findByGenre(String genre);

    Content findByTitle(String title);

    void deleteByTitle(String title);

    List<Content> findByTitleContaining(String word);


}
