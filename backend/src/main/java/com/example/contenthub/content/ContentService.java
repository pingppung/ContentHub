package com.example.contenthub.content;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.contenthub.content.detail.SiteDetail;
import com.example.contenthub.content.dto.ContentDto;
import com.example.contenthub.content.dto.request.ContentSearchRequest;
import com.example.contenthub.content.dto.response.ContentLinkResponse;
import com.example.contenthub.content.enums.ContentType;
import com.example.contenthub.content.enums.Platform;
import com.example.contenthub.content.repository.ContentRepository;
import com.example.contenthub.content.repository.ContentSearchRepository;
import com.example.contenthub.content.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentSearchRepository contentSearchRepository;
    private final SiteRepository siteRepository;
    private final MongoTemplate mongoTemplate;

    public void saveContents(List<ContentDto> contents, String platform, ContentType type) {
        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("platform is required");
        }
        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(contents, "contents is required");
        Platform sourcePlatform = Platform.valueOf(platform);

        contents.forEach(this::validateCollectedContent);
        for (ContentDto dto : contents) {
            saveCollectedContent(dto, sourcePlatform, type);
        }
    }

    private void validateCollectedContent(ContentDto dto) {
        Objects.requireNonNull(dto, "content is required");
        Content.validateTitle(dto.getTitle());
        if (dto.getContentId() == null || dto.getContentId().isBlank()) {
            throw new IllegalArgumentException("cid is required");
        }
    }

    private void saveCollectedContent(ContentDto dto, Platform platform, ContentType type) {
        Optional<Content> existing = contentRepository.findBySource(type, platform, dto.getContentId());
        if (existing.isPresent()) {
            updateCollectedFields(existing.get().getId(), dto, type, platform);
            return;
        }

        Content content = createContent(dto, type, platform);
        try {
            mongoTemplate.insert(content);
        } catch (DuplicateKeyException conflict) {
            Content existingContent = contentRepository.findBySource(type, platform, dto.getContentId())
                    .orElseThrow(() -> conflict);
            updateCollectedFields(existingContent.getId(), dto, type, platform);
        }
    }

    private Content createContent(ContentDto dto, ContentType type, Platform platform) {
        Content content = Content.of(dto.getTitle(), dto.getSynopsis(), dto.getGenre(), dto.getCoverImg(), type);
        content.initializeCollectedData(dto.getDetail());

        SiteDetail site = new SiteDetail(platform, dto.getContentId(), dto.getAgeRating());
        SiteDetail publicationInfo = dto.getPublicationInfo();
        if (publicationInfo != null) {
            site.updateExtraFields(publicationInfo.getExtraFields());
            site.updatePublicationInfo(publicationInfo.getStatus(), publicationInfo.getReleaseDays(),
                    publicationInfo.getScheduleType(), publicationInfo.getEpisodeCount());
        }
        site.markCollectionSucceeded(content.getLastCollectedAt());
        content.addSite(site);
        return content;
    }

    private void updateCollectedFields(String id, ContentDto dto, ContentType type, Platform platform) {
        Query query = Query.query(Criteria.where("id").is(id).and("type").is(type)
                .and("sites").elemMatch(Criteria.where("platform").is(platform)
                        .and("cid").is(dto.getContentId())));
        Update update = buildCollectedFieldsUpdate(dto);
        if (mongoTemplate.updateFirst(query, update, Content.class).getMatchedCount() == 0) {
            throw new IllegalStateException("Content source disappeared during collection: " + id);
        }
    }

    private Update buildCollectedFieldsUpdate(ContentDto dto) {
        Instant now = Instant.now();
        Update update = new Update()
                .set("title", dto.getTitle())
                .set("synopsis", dto.getSynopsis())
                .set("genre", dto.getGenre())
                .set("coverImg", dto.getCoverImg())
                .set("lastCollectedAt", now)
                .set("updatedAt", now)
                .set("sites.$.lastAttemptedAt", now)
                .set("sites.$.lastSucceededAt", now)
                .set("sites.$.collectionStatus", SiteDetail.CollectionStatus.SUCCESS)
                .set("sites.$.ageRating", dto.getAgeRating());
        if (dto.getDetail() != null) {
            update.set("detail", dto.getDetail());
        }
        SiteDetail publicationInfo = dto.getPublicationInfo();
        if (publicationInfo != null) {
            update.set("sites.$.extraFields", publicationInfo.getExtraFields())
                    .set("sites.$.status", publicationInfo.getStatus())
                    .set("sites.$.releaseDays", publicationInfo.getReleaseDays())
                    .set("sites.$.scheduleType", publicationInfo.getScheduleType())
                    .set("sites.$.episodeCount", publicationInfo.getEpisodeCount());
            if (publicationInfo.getExtraFields().containsKey("availability_status")) {
                update.set("sites.$.availabilityStatus", publicationInfo.getAvailabilityStatus());
            }
        }
        return update;
    }

    public List<ContentDto> filterContents(ContentSearchRequest searchRequest) {
        List<Content> contents = contentSearchRepository.search(
                searchRequest.getType(), searchRequest.getGenre(), searchRequest.getTitle());
        return mapToContentDTOList(contents);
    }

    public List<ContentDto> filterById(List<String> ids) {
        return mapToContentDTOList(contentRepository.findAllById(ids));
    }

    public List<ContentDto> mapToContentDTOList(List<Content> contents) {
        List<ContentDto> result = new ArrayList<>(contents.size());
        for (Content content : contents) {
            result.add(toContentDto(content));
        }
        return result;
    }

    private ContentDto toContentDto(Content content) {
        List<ContentLinkResponse> links = new ArrayList<>(content.getSites().size());
        for (SiteDetail site : content.getSites()) {
            links.add(toLink(site, content.getType()));
        }
        return ContentDto.builder()
                .contentId(content.getId())
                .type(content.getType())
                .detail(content.getDetail())
                .title(content.getTitle())
                .synopsis(content.getSynopsis())
                .coverImg(content.getCoverImg())
                .genre(content.getGenre())
                .links(links)
                .build();
    }

    private ContentLinkResponse toLink(SiteDetail site, ContentType type) {
        Site platformSite = siteRepository.findByPlatformAndContentType(site.getPlatform(), type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid platform: " + site.getPlatform()));
        String url = platformSite.getUrlFormat() + site.getCid();
        return new ContentLinkResponse(site.getPlatform().name(), url, site.getAgeRating());
    }
}
