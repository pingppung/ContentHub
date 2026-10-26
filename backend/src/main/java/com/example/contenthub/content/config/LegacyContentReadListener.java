package com.example.contenthub.content.config;

import java.util.List;
import java.util.Locale;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.stereotype.Component;

import com.example.contenthub.content.Content;

/** Adapts legacy documents in memory without rewriting stored MongoDB data. */
@Component
public class LegacyContentReadListener extends AbstractMongoEventListener<Content> {

    @Override
    public void onAfterLoad(AfterLoadEvent<Content> event) {
        Document document = event.getSource();
        if (document.get("type") == null && document.get("category") instanceof String category) {
            document.put("type", category.trim().toUpperCase(Locale.ROOT));
        }
        if (document.get("sites") instanceof List<?> sites) {
            for (Object value : sites) {
                if (value instanceof Document site) {
                    if (site.get("platform") instanceof String platform) {
                        site.put("platform", canonicalPlatform(platform));
                    }
                    if (!site.containsKey("ageRating") && site.containsKey("age_rating")) {
                        site.put("ageRating", site.get("age_rating"));
                    }
                }
            }
        }
    }

    private String canonicalPlatform(String value) {
        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "카카오페이지", "KAKAOPAGE", "KAKAO_PAGE" -> "KAKAO_PAGE";
            case "네이버시리즈", "NAVERSERIES", "NAVER_SERIES" -> "NAVER_SERIES";
            case "네이버웹툰", "NAVERWEBTOON", "NAVER_WEBTOON" -> "NAVER_WEBTOON";
            case "웨이브", "WAVVE" -> "WAVVE";
            default -> value;
        };
    }
}
