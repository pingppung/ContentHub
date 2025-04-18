package com.example.contenthub.service.crawling;

import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.SiteRepository;
import com.example.contenthub.utils.ContentCrawlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.SiteDetail;
import com.example.contenthub.domain.Site;
import com.example.contenthub.dto.ContentCrawlDTO;
import com.example.contenthub.dto.ContentResponseDTO;
import com.example.contenthub.dto.LinkDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final ContentRepository contentRepository;
    private final SiteRepository siteRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public void crawl() throws IOException {
        String pythonUrl = "http://localhost:5000/crawl";
        System.out.println(pythonUrl);
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(pythonUrl,
                JsonNode.class);
        JsonNode body = response.getBody();
        String status = body.get("status").asText();
        System.out.println(status);
        if (status.equals("success")) {
            Iterator<String> fieldNamesIterator = body.get("data").fieldNames();
            while (fieldNamesIterator.hasNext()) {
                String platform = fieldNamesIterator.next();
                JsonNode platformData = body.get("data").get(platform);
                List<ContentCrawlDTO> contentList = ContentCrawlUtils.convertToDTOList(platformData);
                System.out.println(contentList.toString());
                saveContents(contentList, platform, "novel");
            }
        }
    }

    @Transactional
    public void saveContents(List<ContentCrawlDTO> contents, String platform, String category) {
        for (ContentCrawlDTO content : contents) {
            Content contentToSave = checkContentExists(category, content.getTitle());
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

    public void saveContentSite(Content content, String platform, String contentID, boolean isAdultContent) {
        SiteDetail newSite = new SiteDetail(platform, contentID, isAdultContent);
        content.addSite(newSite);
        contentRepository.save(content);
    }

    public Content checkContentExists(String category, String title) {
        return contentRepository.findByTitleAndCategory(title, category);
    }

    public List<ContentResponseDTO> getContentsFilter(String genre, String title, String category) {
        if ("전체".equals(genre)) {
            return (title != null) ? getContentsByCategoryAndTitle(category, title)
                    : getContentsByCategory(category);
        } else {
            return (title != null) ? getContentsByCategoryAndGenreAndTitle(category, genre, title)
                    : getContentsByCategoryAndGenre(category, genre);
        }
    }

    // 카테고리만 필터링
    private List<ContentResponseDTO> getContentsByCategory(String category) {
        List<Content> list = contentRepository.findByCategory(category);
        return getContentDTOList(list);
    }

    // 제목 + 카테고리 필터링
    private List<ContentResponseDTO> getContentsByCategoryAndTitle(String title, String category) {
        List<Content> list = contentRepository.findByCategoryAndTitleContaining(title, category);
        return getContentDTOList(list);
    }

    // 제목 + 장르 + 카테고리 필터링
    private List<ContentResponseDTO> getContentsByCategoryAndGenreAndTitle(String title, String genre,
            String category) {
        List<Content> list = contentRepository.findByCategoryAndGenreAndTitleContaining(title, genre, category);
        return getContentDTOList(list);
    }

    // 장르 + 카테고리 필터링
    private List<ContentResponseDTO> getContentsByCategoryAndGenre(String genre, String category) {
        List<Content> list = contentRepository.findByCategoryAndGenre(genre, category);
        return getContentDTOList(list);
    }

    // 넘겨주기 위해서 db에서 찾은 후 dto로 변환시키는 메서드
    public List<ContentResponseDTO> getContentDTOList(List<Content> list) {
        List<ContentResponseDTO> contents = new ArrayList<>();
        for (Content n : list) {
            ContentResponseDTO content = new ContentResponseDTO(
                    n.getTitle(),
                    n.getDescription(),
                    n.getCoverImg(),
                    n.getGenre());
            for (SiteDetail site : n.getSites()) {
                String format = siteRepository.findByPlatform(site.getPlatform()).getUrlFormat();
                String url = format + site.getContentID();
                LinkDTO linkDTO = new LinkDTO(site.getPlatform(), url, site.isAdult());
                content.getLinks().add(linkDTO);
            }
            contents.add(content);
        }
        return contents;
    }

}