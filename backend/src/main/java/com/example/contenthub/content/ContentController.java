package com.example.contenthub.content;

import com.example.contenthub.content.enums.ContentType;

import com.example.contenthub.content.dto.ContentDto;

import com.example.contenthub.content.dto.request.ContentSearchRequest;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    // /search and category remain aliases for existing clients.
    @GetMapping({"", "/search"})
    public ResponseEntity<List<ContentDto>> getContents(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "title", required = false) String title) {
        ContentSearchRequest request = new ContentSearchRequest(
                resolveType(type, category), trimToNull(genre), trimToNull(title));
        return ResponseEntity.ok(contentService.filterContents(request));
    }

    private ContentType resolveType(String type, String category) {
        ContentType requestedType = parseType(type);
        ContentType legacyType = parseType(category);
        if (requestedType != null && legacyType != null && requestedType != legacyType) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type과 category는 같은 콘텐츠 타입이어야 합니다.");
        }
        return requestedType != null ? requestedType : legacyType;
    }

    private ContentType parseType(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            return null;
        }
        try {
            return ContentType.valueOf(normalized.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException invalid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "콘텐츠 타입은 NOVEL, WEBTOON, MOVIE, DRAMA 중 하나여야 합니다.", invalid);
        }
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
