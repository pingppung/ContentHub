package com.example.contenthub.repository;

import com.example.contenthub.crawling.NovelData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends MongoRepository<NovelData, String> {

}
