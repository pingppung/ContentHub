package com.example.contenthub.service.content;

import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.SiteRepository;
import com.example.contenthub.domain.SiteDetail;
import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.Site;
import com.example.contenthub.dto.ContentCrawlDTO;
import com.example.contenthub.dto.ContentResponseDTO;
import com.example.contenthub.dto.LinkDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    private final SiteRepository siteRepository;

    @Transactional
    public void saveContents(List<ContentCrawlDTO> contents, String platform, String category) {
        for (ContentCrawlDTO content : contents) {
            Content contentToSave = findExistingContent(category, content.getTitle());
            if (contentToSave == null) {
                contentToSave = new Content(
                        content.getTitle(),
                        content.getDescription(),
                        content.getGenre(),
                        content.getCoverImg(),
                        category);

                contentRepository.save(contentToSave);
            }
            saveContentSite(contentToSave, platform, content.getContentId(), content.isAdultContent());

        }
    }

    public void saveContentSite(Content content, String platform,
            String contentID, boolean isAdultContent) {
        SiteDetail newSite = new SiteDetail(platform, contentID, isAdultContent);
        content.addSite(newSite);
    }

    public Content findExistingContent(String category, String title) {
        return contentRepository.findByCategoryAndTitle(category, title);
    }

    public List<ContentResponseDTO> filterContents(String genre, String title, String category) {
        if ("전체".equals(genre)) {
            return (title != null) ? filterByCategoryAndTitle(category, title)
                    : filterByCategory(category);
        } else {
            return (title != null) ? filterByCategoryGenreAndTitle(category, genre, title)
                    : filterByCategoryAndGenre(category, genre);
        }
    }

    // 카테고리만 필터링
    private List<ContentResponseDTO> filterByCategory(String category) {
        List<Content> list = contentRepository.findByCategory(category);
        return mapToContentDTOList(list);
    }

    // 제목 + 카테고리 필터링
    private List<ContentResponseDTO> filterByCategoryAndTitle(String title, String category) {
        List<Content> list = contentRepository.findByCategoryAndTitleContaining(title, category);
        return mapToContentDTOList(list);
    }

    // 제목 + 장르 + 카테고리 필터링
    private List<ContentResponseDTO> filterByCategoryGenreAndTitle(String title, String genre,
            String category) {
        List<Content> list = contentRepository.findByCategoryAndGenreAndTitleContaining(title, genre, category);
        return mapToContentDTOList(list);
    }

    // 장르 + 카테고리 필터링
    private List<ContentResponseDTO> filterByCategoryAndGenre(String genre, String category) {
        List<Content> list = contentRepository.findByCategoryAndGenre(genre, category);
        return mapToContentDTOList(list);
    }

    // 넘겨주기 위해서 db에서 찾은 후 dto로 변환시키는 메서드
    public List<ContentResponseDTO> mapToContentDTOList(List<Content> list) {
        List<ContentResponseDTO> contents = new ArrayList<>();
        for (Content n : list) {
            ContentResponseDTO content = new ContentResponseDTO(
                    n.getTitle(),
                    n.getDescription(),
                    n.getCoverImg(),
                    n.getGenre());
            for (SiteDetail site : n.getSites()) {
                Site platformSite = siteRepository.findByPlatform(site.getPlatform());
                if (platformSite == null) {
                    throw new IllegalArgumentException("Invalid platform: " + site.getPlatform());
                }
                String url = platformSite.getUrlFormat() + site.getCid();
                LinkDTO linkDTO = new LinkDTO(site.getPlatform(), url, site.isAdult());
                content.getLinks().add(linkDTO);
            }
            contents.add(content);
        }
        return contents;
    }

}