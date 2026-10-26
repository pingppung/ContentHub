package com.example.contenthub.content;

import com.example.contenthub.content.enums.Platform;

import com.example.contenthub.content.enums.ContentStatus;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.ContentDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.contenthub.content.detail.NovelDetail;
import com.example.contenthub.content.detail.SiteDetail;
import com.example.contenthub.content.repository.ContentRepository;
import com.example.contenthub.content.repository.ContentSearchRepository;
import com.example.contenthub.content.repository.SiteRepository;
import com.mongodb.client.result.UpdateResult;

class ContentServiceTest {
    private final ContentRepository repository = mock(ContentRepository.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final SiteRepository sites = mock(SiteRepository.class);
    private final ContentService service = new ContentService(repository,
            mock(ContentSearchRepository.class), sites, mongoTemplate);

    @BeforeEach
    void setUp() {
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Content.class)))
                .thenReturn(UpdateResult.acknowledged(1, 1L, null));
    }

    private ContentDto collectedContent() {
        SiteDetail info = new SiteDetail();
        info.updatePublicationInfo(ContentStatus.ONGOING, null, SiteDetail.ScheduleType.UNKNOWN, 10);
        return ContentDto.builder().title("Title").contentId("123").ageRating("12")
                .detail(new NovelDetail(List.of("Author"), null)).publicationInfo(info).build();
    }

    private Content existingContent() {
        Content content = Content.of("Old title", null, List.of(), null, ContentType.NOVEL);
        ReflectionTestUtils.setField(content, "id", "existing-id");
        return content;
    }

    @Test
    void queryResponseContainsInternalIdTypeAndStoredDetail() {
        Content content = existingContent();
        NovelDetail detail = new NovelDetail(List.of("Author"), null);
        content.initializeCollectedData(detail);
        when(repository.findAllById(List.of("existing-id"))).thenReturn(List.of(content));

        ContentDto response = service.filterById(List.of("existing-id")).get(0);

        assertEquals("existing-id", response.getContentId());
        assertEquals(ContentType.NOVEL, response.getType());
        assertSame(detail, response.getDetail());
        assertEquals("Old title", response.getTitle());
        assertEquals(List.of(), response.getLinks());
    }

    @Test
    void newSourceIsInsertedWithDetailAndCollectionMetadata() {
        when(repository.findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123"))
                .thenReturn(Optional.empty());
        service.saveContents(List.of(collectedContent()), "KAKAO_PAGE", ContentType.NOVEL);
        ArgumentCaptor<Content> inserted = ArgumentCaptor.forClass(Content.class);
        verify(mongoTemplate).insert(inserted.capture());
        Content content = inserted.getValue();
        assertInstanceOf(NovelDetail.class, content.getDetail());
        assertNotNull(content.getCreatedAt());
        assertEquals(content.getLastCollectedAt(), content.getUpdatedAt());
        SiteDetail source = content.getSites().get(0);
        assertEquals("123", source.getCid());
        assertEquals(10, source.getEpisodeCount());
        assertEquals(SiteDetail.CollectionStatus.SUCCESS, source.getCollectionStatus());
        assertEquals(SiteDetail.AvailabilityStatus.UNKNOWN, source.getAvailabilityStatus());
        assertEquals(content.getLastCollectedAt(), source.getFirstCollectedAt());
        verify(mongoTemplate, never()).updateFirst(any(Query.class), any(Update.class), eq(Content.class));
    }

    @Test
    void existingSourceUsesElemMatchAndPartialUpdate() {
        when(repository.findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123"))
                .thenReturn(Optional.of(existingContent()));
        service.saveContents(List.of(collectedContent()), "KAKAO_PAGE", ContentType.NOVEL);
        ArgumentCaptor<Query> query = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> update = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate).updateFirst(query.capture(), update.capture(), eq(Content.class));
        Document match = query.getValue().getQueryObject().get("sites", Document.class)
                .get("$elemMatch", Document.class);
        assertEquals(Platform.KAKAO_PAGE, match.get("platform"));
        assertEquals("123", match.getString("cid"));
        Document fields = update.getValue().getUpdateObject().get("$set", Document.class);
        assertEquals(ContentStatus.ONGOING, fields.get("sites.$.status"));
        assertEquals(10, fields.get("sites.$.episodeCount"));
        assertFalse(fields.containsKey("sites"));
        assertFalse(fields.containsKey("createdAt"));
        assertFalse(fields.containsKey("sites.$.availabilityStatus"));
        assertEquals(fields.get("updatedAt"), fields.get("lastCollectedAt"));
        verify(mongoTemplate, never()).insert(any(Content.class));
    }

    @Test
    void concurrentInsertConflictReloadsAndUpdatesWinner() {
        when(repository.findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existingContent()));
        when(mongoTemplate.insert(any(Content.class))).thenThrow(new DuplicateKeyException("source"));
        service.saveContents(List.of(collectedContent()), "KAKAO_PAGE", ContentType.NOVEL);
        verify(repository, times(2)).findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123");
        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Content.class));
    }

    @Test
    void unrelatedDuplicateKeyConflictIsPropagated() {
        when(repository.findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123"))
                .thenReturn(Optional.empty());
        DuplicateKeyException conflict = new DuplicateKeyException("other index");
        when(mongoTemplate.insert(any(Content.class))).thenThrow(conflict);
        assertSame(conflict, assertThrows(DuplicateKeyException.class,
                () -> service.saveContents(List.of(collectedContent()), "KAKAO_PAGE", ContentType.NOVEL)));
    }

    @Test
    void invalidLaterItemPreventsAnyBatchWrites() {
        ContentDto invalid = collectedContent();
        invalid.setContentId(" ");
        assertThrows(IllegalArgumentException.class, () -> service.saveContents(
                List.of(collectedContent(), invalid), "KAKAO_PAGE", ContentType.NOVEL));
        verifyNoInteractions(repository);
        verify(mongoTemplate, never()).insert(any(Content.class));
        verify(mongoTemplate, never()).updateFirst(any(Query.class), any(Update.class), eq(Content.class));
    }

    @Test
    void absentDetailsAreNotClearedByPartialUpdate() {
        when(repository.findBySource(ContentType.NOVEL, Platform.KAKAO_PAGE, "123"))
                .thenReturn(Optional.of(existingContent()));
        ContentDto dto = collectedContent();
        dto.setDetail(null);
        dto.setPublicationInfo(null);
        service.saveContents(List.of(dto), "KAKAO_PAGE", ContentType.NOVEL);
        ArgumentCaptor<Update> update = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate).updateFirst(any(Query.class), update.capture(), eq(Content.class));
        Document fields = update.getValue().getUpdateObject().get("$set", Document.class);
        assertFalse(fields.containsKey("detail"));
        assertFalse(fields.containsKey("sites.$.status"));
    }
}
