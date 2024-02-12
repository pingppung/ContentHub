package com.example.contenthub.repository;

import com.example.contenthub.crawling.novel.NovelData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends MongoRepository<NovelData, String> {

    List<NovelData> findByGenre(String genre);

    NovelData findByTitle(String title);

    void deleteByTitle(String title);

    List<NovelData> findByTitleContaining(String word);
}
