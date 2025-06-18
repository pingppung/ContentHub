package com.example.contenthub.service.content;

import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.SiteRepository;
import com.example.contenthub.domain.SiteDetail;
import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.Site;
import com.example.contenthub.dto.ContentDataDTO;
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
    public void saveContents(List<ContentDataDTO> contents, String platform, String category) {
        for (ContentDataDTO content : contents) {
            Content contentToSave = findExistingContent(category, content.getTitle());
            if (contentToSave == null) {
                contentToSave = new Content(
                        content.getTitle(),
                        content.getSynopsis(),
                        content.getGenre(),
                        content.getCoverImg(),
                        category);

                if (category.equalsIgnoreCase("movie")) {
                    contentToSave.setActors(content.getActors());
                    contentToSave.setCountry(content.getCountry());
                } else if (category.equalsIgnoreCase("drama")) {
                    contentToSave.setReleaseDate(content.getReleaseDate());
                    contentToSave.setStatus(content.getStatus());
                } else if (category.equalsIgnoreCase("webtoon")) {
                    contentToSave.setPublish(content.getPublish());
                }
                contentRepository.save(contentToSave);
            }
            saveContentSite(contentToSave, platform, content.getContentId(), content.getAgeRating());

        }
    }

    public void saveContentSite(Content content, String platform,
            String contentID, String ageRating) {
        SiteDetail newSite = new SiteDetail(platform, contentID, ageRating);
        content.addSite(newSite);
        contentRepository.save(content);
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
                    n.getSynopsis(),
                    n.getCoverImg(),
                    n.getGenre());
            for (SiteDetail site : n.getSites()) {
                Site platformSite = siteRepository.findByPlatformAndCategory(site.getPlatform(), n.getCategory());
                if (platformSite == null) {
                    throw new IllegalArgumentException("Invalid platform: " + site.getPlatform());
                }
                String url = platformSite.getUrlFormat() + site.getCid();
                LinkDTO linkDTO = new LinkDTO(site.getPlatform(), url, site.getAge_rating());
                content.getLinks().add(linkDTO);
            }
            contents.add(content);
        }
        return contents;
    }

}