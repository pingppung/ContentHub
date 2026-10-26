package com.example.contenthub.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;

import com.example.contenthub.content.config.LegacyContentReadListener;
import com.example.contenthub.content.enums.ContentType;
import com.example.contenthub.content.enums.Platform;

class LegacyContentReadListenerTest {

    @Test
    void legacyDocumentCanBeMappedWithoutEnumConversionFailure() throws Exception {
        Document site = new Document("platform", "네이버시리즈")
                .append("cid", "123").append("age_rating", "15");
        Document document = new Document("category", "novel")
                .append("title", "Legacy title").append("sites", List.of(site));
        new LegacyContentReadListener().onAfterLoad(new AfterLoadEvent<>(document, Content.class, "contents"));
        MongoMappingContext context = new MongoMappingContext();
        MongoCustomConversions conversions = MongoCustomConversions.create(adapter -> {});
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        context.afterPropertiesSet();
        MappingMongoConverter converter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, context);
        converter.setCustomConversions(conversions);
        converter.afterPropertiesSet();

        Content content = converter.read(Content.class, document);

        assertEquals(ContentType.NOVEL, content.getType());
        assertEquals(Platform.NAVER_SERIES, content.getSites().get(0).getPlatform());
        assertEquals("15", content.getSites().get(0).getAgeRating());
    }

    @Test
    void canonicalValuesTakePrecedenceOverLegacyFields() {
        Document site = new Document("platform", "WAVVE").append("ageRating", "12").append("age_rating", "15");
        Document document = new Document("type", "MOVIE").append("category", "novel")
                .append("sites", List.of(site));
        new LegacyContentReadListener().onAfterLoad(new AfterLoadEvent<>(document, Content.class, "contents"));
        assertEquals("MOVIE", document.get("type"));
        assertEquals("WAVVE", site.get("platform"));
        assertEquals("12", site.get("ageRating"));
        assertFalse(document.containsKey("detail"));
    }
}
