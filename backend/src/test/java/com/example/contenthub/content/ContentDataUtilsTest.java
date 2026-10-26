package com.example.contenthub.content;

import com.example.contenthub.content.enums.ContentStatus;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.ContentDto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.contenthub.content.collector.ContentDataUtils;
import com.example.contenthub.content.detail.*;
import com.fasterxml.jackson.databind.ObjectMapper;

class ContentDataUtilsTest {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private ContentDto convert(String json, ContentType type) throws Exception {
        return ContentDataUtils.convertToDTOList(mapper.readTree("[" + json + "]"), type).get(0);
    }

    @Test
    void novelSeparatesPlatformPublicationDataFromWorkMetadata() throws Exception {
        ContentDto response = convert("""
                {"author":"Author", "status":"ONGOING", "episode_count":12,
                 "publish_days":["MONDAY"], "first_published_date":"2024-05-06"}
                """, ContentType.NOVEL);
        NovelDetail detail = (NovelDetail) response.getDetail();
        assertEquals(List.of("Author"), detail.getAuthors());
        assertEquals(LocalDate.of(2024, 5, 6), detail.getFirstPublishedDate());
        assertEquals(ContentStatus.ONGOING, response.getPublicationInfo().getStatus());
        assertEquals(List.of(DayOfWeek.MONDAY), response.getPublicationInfo().getReleaseDays());
        assertEquals(12, response.getPublicationInfo().getEpisodeCount());
    }

    @Test
    void movieAcceptsMultipleCreatorsWithoutInventingMissingDateOrRating() throws Exception {
        ContentDto response = convert("""
                {"directors":["A","B"], "country":"KR", "release_year":2024}
                """, ContentType.MOVIE);
        MovieDetail detail = (MovieDetail) response.getDetail();
        assertEquals(List.of("A", "B"), detail.getDirectors());
        assertEquals(List.of("KR"), detail.getCountries());
        assertNull(detail.getReleaseDate());
        assertNull(detail.getDurationMinutes());
        assertNull(response.getAgeRating());
    }

    @Test
    void dramaKeepsPlannedTotalSeparateFromPlatformAvailableCount() throws Exception {
        ContentDto response = convert("""
                {"episode_count":4,"total_episode_count":16,"broadcaster":"TV",
                 "broadcast_days":["FRIDAY"]}
                """, ContentType.DRAMA);
        DramaDetail detail = (DramaDetail) response.getDetail();
        assertEquals(16, detail.getEpisodeCount());
        assertEquals(4, response.getPublicationInfo().getEpisodeCount());
        assertEquals(List.of("TV"), detail.getBroadcasters());
        assertEquals(List.of(DayOfWeek.FRIDAY), detail.getBroadcastDays());
        assertNull(response.getPublicationInfo().getReleaseDays());
    }

    @Test
    void publicationStatusIsExposedOnlyInPlatformMetadata() throws Exception {
        for (ContentType type : List.of(ContentType.NOVEL, ContentType.WEBTOON, ContentType.DRAMA)) {
            ContentDto response = convert("{\"status\":\"COMPLETED\"}", type);
            assertFalse(mapper.valueToTree(response.getDetail()).has("status"), type.name());
            assertEquals(ContentStatus.COMPLETED, response.getPublicationInfo().getStatus(), type.name());
        }
    }

    @Test
    void invalidCountsAndRuntimeAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> convert("{\"episode_count\":-1}", ContentType.WEBTOON));
        assertThrows(IllegalArgumentException.class,
                () -> convert("{\"episode_count\":\"unknown\"}", ContentType.NOVEL));
        assertThrows(IllegalArgumentException.class,
                () -> convert("{\"duration_minutes\":0}", ContentType.MOVIE));
    }

    @Test
    void preservesExtraCollectedFields() throws Exception {
        ContentDto response = convert("""
                {"extra_fields":{"publisher":"출판사","rating":9.5,"rating_max":10}}
                """, ContentType.NOVEL);
        assertEquals("출판사", response.getPublicationInfo().getExtraFields().get("publisher"));
        assertEquals(9.5, response.getPublicationInfo().getExtraFields().get("rating"));
    }
}
