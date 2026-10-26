package com.example.contenthub.content.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.contenthub.content.Content;
import com.example.contenthub.content.enums.ContentType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ContentSearchRepositoryImpl implements ContentSearchRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Content> search(
            ContentType type,
            String genre,
            String title
    ) {

        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        if (type != null) {
            criteria.add(
                    new Criteria().orOperator(
                            Criteria.where("type").is(type),
                            new Criteria().andOperator(
                                    Criteria.where("type").is(null),
                                    Criteria.where("category").regex("^" + type.name() + "$", "i")))
            );
        }

        if (genre != null && !genre.isBlank()) {
            criteria.add(
                    Criteria.where("genre").in(genre)
            );
        }

        if (title != null && !title.isBlank()) {
            criteria.add(
                    Criteria.where("title")
                            .regex(title, "i")
            );
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            criteria.toArray(new Criteria[0])
                    )
            );
        }

        return mongoTemplate.find(query, Content.class);
    }
}
