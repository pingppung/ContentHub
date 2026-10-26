package com.example.contenthub.content.collector;

import com.example.contenthub.content.enums.ContentStatus;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.ContentDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.example.contenthub.content.detail.ContentDetail;
import com.example.contenthub.content.detail.DramaDetail;
import com.example.contenthub.content.detail.MovieDetail;
import com.example.contenthub.content.detail.NovelDetail;
import com.example.contenthub.content.detail.WebtoonDetail;
import com.example.contenthub.content.detail.SiteDetail;
import com.fasterxml.jackson.databind.JsonNode;

public class ContentDataUtils {

    public static List<ContentDto> convertToDTOList(JsonNode nodeList, ContentType type) {

        List<ContentDto> contentList = new ArrayList<>();

        if (nodeList == null) {
            return contentList;
        }

        for (JsonNode node : nodeList) {

            ContentDetail detail = createDetail(node, type);

            ContentDto content = ContentDto.builder()
                    .title(getText(node, "title"))
                    .synopsis(getText(node, "synopsis"))
                    .coverImg(getText(node, "cover_img"))
                    .genre(convertList(node.get("genre")))
                    .ageRating(getText(node, "age_rating"))
                    .contentId(getText(node, "content_id"))
                    .type(type)
                    .detail(detail)
                    .publicationInfo(createPublicationInfo(node, type))
                    .build();

            contentList.add(content);

        }

        return contentList;

    }

    private static ContentDetail createDetail(JsonNode node, ContentType type) {
        return switch (type) {

            case MOVIE -> new MovieDetail(
                    getNames(node, "countries", "country"),
                    convertList(node.get("actors")),
                    getInteger(node, "release_year"),
                    getPositiveInteger(node, "duration_minutes"),
                    getNames(node, "directors", "director"),
                    getDate(node, "release_date")
            );

            case DRAMA -> new DramaDetail(
                    getInteger(node, "release_year"),
                    convertList(node.get("actors")),
                    getInteger(node, "season_count"),
                    getNames(node, "broadcasters", "broadcaster"),
                    convertDayList(node.get("broadcast_days")),
                    getInteger(node, "total_episode_count"),
                    getDate(node, "first_aired_date"),
                    getDate(node, "last_aired_date")
            );
            case WEBTOON -> new WebtoonDetail(
                    getNames(node, "writers", "writer"),
                    getNames(node, "artists", "artist"),
                    getDate(node, "first_published_date")
            );

            case NOVEL -> new NovelDetail(
                    getNames(node, "authors", "author"),
                    getDate(node, "first_published_date")
            );
        };
    }


    private static Integer getInteger(JsonNode node, String field) {
        JsonNode value = node.get(field);

        if (value == null || value.isNull()) {
            return null;
        }

        if (!value.isIntegralNumber() && !value.isTextual()) {
            throw new IllegalArgumentException(field + " must be an integer");
        }
        int result;
        try {
            result = Integer.parseInt(value.asText());
        } catch (NumberFormatException invalid) {
            throw new IllegalArgumentException(field + " must be an integer", invalid);
        }
        if (result < 0) {
            throw new IllegalArgumentException(field + " cannot be negative");
        }
        return result;
    }

    private static Integer getPositiveInteger(JsonNode node, String field) {
        Integer value = getInteger(node, field);
        if (value != null && value == 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
        return value;
    }

    private static LocalDate getDate(JsonNode node, String field) {
        String value = getText(node, field);
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }

    private static List<String> getNames(JsonNode node, String plural, String singular) {
        return convertList(node.hasNonNull(plural) ? node.get(plural) : node.get(singular));
    }

    private static SiteDetail createPublicationInfo(JsonNode node, ContentType type) {
        SiteDetail info = new SiteDetail();
        JsonNode extraFields = node.get("extra_fields");
        if (extraFields != null && !extraFields.isNull()) {
            if (!extraFields.isObject()) {
                throw new IllegalArgumentException("extra_fields must be an object");
            }
            Map<String, Object> fields = new ObjectMapper().convertValue(
                    extraFields, new TypeReference<Map<String, Object>>() {});
            info.updateExtraFields(fields);
        }
        JsonNode days = node.hasNonNull("release_days") ? node.get("release_days") : node.get("publish_days");
        // Broadcast schedule belongs to DramaDetail; platform release schedule stays here.
        List<DayOfWeek> releaseDays = days == null || days.isNull() ? null : convertDayList(days);
        String rawSchedule = getText(node, "schedule_type");
        SiteDetail.ScheduleType schedule = SiteDetail.ScheduleType.UNKNOWN;
        if (rawSchedule != null) {
            try {
                schedule = SiteDetail.ScheduleType.valueOf(rawSchedule.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                // Preserve unknown schedules rather than guessing regularity.
            }
        }
        info.updatePublicationInfo(type == ContentType.MOVIE ? ContentStatus.UNKNOWN : getStatus(node),
                releaseDays, schedule, getInteger(node, "episode_count"));
        return info;
    }

    private static ContentStatus getStatus(JsonNode node) {
        String status = getText(node, "status");

        if (status == null) {
            return ContentStatus.UNKNOWN;
        }

        return switch (status.toUpperCase(Locale.ROOT)) {
            case "ONGOING" -> ContentStatus.ONGOING;
            case "HIATUS" -> ContentStatus.HIATUS;
            case "COMPLETED" -> ContentStatus.COMPLETED;
            case "UPCOMING" -> ContentStatus.UPCOMING;
            default -> ContentStatus.UNKNOWN;
        };
    }
    private static String getText(JsonNode node, String field) {
        return node.hasNonNull(field)
                ? node.get(field).asText()
                : null;
    }

    private static List<String> convertList(JsonNode node) {
        List<String> list = new ArrayList<>();

        if (node == null || node.isNull()) {
            return list;
        }

        if (node.isTextual()) {
            if (!node.asText().isBlank()) {
                list.add(node.asText().trim());
            }
            return list;
        }
        if (!node.isArray()) {
            throw new IllegalArgumentException("Expected a string or an array of strings");
        }

        for (JsonNode value : node) {
            if (value.isTextual() && !value.asText().isBlank()) {
                list.add(value.asText().trim());
            }
        }

        return list;
    }

    private static List<DayOfWeek> convertDayList(JsonNode node) {
        if (node == null || !node.isArray()) {
            return List.of();
        }

        List<DayOfWeek> days = new ArrayList<>();

        for (JsonNode item : node) {
            try {
                days.add(DayOfWeek.valueOf(item.asText().toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {
            }
        }

        return days;
    }

}


