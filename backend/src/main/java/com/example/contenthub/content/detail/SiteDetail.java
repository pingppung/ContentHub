package com.example.contenthub.content.detail;

import com.example.contenthub.content.enums.Platform;

import java.util.Objects;
import java.time.Instant;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import com.example.contenthub.content.enums.ContentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SiteDetail {

    private Platform platform;
    private String cid;
    private String ageRating;
    private Map<String, Object> extraFields = Map.of();

    public void updateExtraFields(Map<String, Object> fields) {
        this.extraFields = fields == null ? Map.of() : Map.copyOf(fields);
        Object availability = this.extraFields.get("availability_status");
        if (availability instanceof String value) {
            this.availabilityStatus = AvailabilityStatus.valueOf(value);
        }
    }

    // Publication status reported by this platform; the only stored publication status.
    private ContentStatus status = ContentStatus.UNKNOWN;
    private List<DayOfWeek> releaseDays;
    private ScheduleType scheduleType = ScheduleType.UNKNOWN;
    // Episodes currently available on this platform, not planned total episodes.
    private Integer episodeCount;

    public enum ScheduleType {
        UNKNOWN, REGULAR, IRREGULAR
    }

    public void updatePublicationInfo(ContentStatus status, List<DayOfWeek> releaseDays,
            ScheduleType scheduleType, Integer episodeCount) {
        if (episodeCount != null && episodeCount < 0) {
            throw new IllegalArgumentException("episodeCount cannot be negative");
        }
        this.status = status == null ? ContentStatus.UNKNOWN : status;
        this.releaseDays = releaseDays == null ? null : List.copyOf(releaseDays);
        this.scheduleType = scheduleType == null ? ScheduleType.UNKNOWN : scheduleType;
        this.episodeCount = episodeCount;
    }

    private Instant firstCollectedAt;
    private Instant lastAttemptedAt;
    private Instant lastSucceededAt;
    private CollectionStatus collectionStatus = CollectionStatus.UNKNOWN;
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.UNKNOWN;

    public enum CollectionStatus {
        UNKNOWN, SUCCESS, FAILED
    }

    public enum AvailabilityStatus {
        UNKNOWN, AVAILABLE, UNAVAILABLE, REMOVED
    }

    public SiteDetail(Platform platform, String cid, String ageRating) {
        this.platform = Objects.requireNonNull(platform, "platform is required");
        if (cid == null || cid.isBlank()) {
            throw new IllegalArgumentException("cid is required");
        }
        this.cid = cid;
        this.ageRating = ageRating;
    }

    public void markCollectionSucceeded(Instant collectedAt) {
        Objects.requireNonNull(collectedAt, "collectedAt is required");
        if (firstCollectedAt == null) {
            firstCollectedAt = collectedAt;
        }
        lastAttemptedAt = collectedAt;
        lastSucceededAt = collectedAt;
        collectionStatus = CollectionStatus.SUCCESS;
        // Successful crawling alone does not prove that the work is available.
    }
}
