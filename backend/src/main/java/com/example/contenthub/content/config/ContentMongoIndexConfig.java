package com.example.contenthub.content.config;

import com.example.contenthub.content.Content;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class ContentMongoIndexConfig {

    @Bean
    InitializingBean contentSourceUniqueIndex(MongoTemplate mongoTemplate) {
        // Fail startup if existing data cannot satisfy source uniqueness.
        return () -> mongoTemplate.indexOps(Content.class).ensureIndex(new Index()
                .on("type", Direction.ASC)
                .on("sites.platform", Direction.ASC)
                .on("sites.cid", Direction.ASC)
                .unique()
                .named("uq_content_source"));
    }
}
