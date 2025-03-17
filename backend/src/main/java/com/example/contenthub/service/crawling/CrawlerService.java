package com.example.contenthub.service.crawling;

import com.example.contenthub.entity.Content;
import com.example.contenthub.entity.ContentSite;
import com.example.contenthub.entity.Site;
import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.SiteRepository;
import com.example.contenthub.service.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.service.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.dto.ContentCrawlDTO;
import com.example.contenthub.dto.ContentResponseDTO;
import com.example.contenthub.dto.LinkDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final ContentRepository contentRepository;
    private final SiteRepository siteRepository;
    private final NaverSeriesCrawler naverSeriesCrawler;
    private final KakaoPageCrawler kakaoPageCrawler;

    public void crawl() throws IOException {
        WebDriver driver = CustomWebDriverManager.getDriver();

        try {
            long startTime = System.currentTimeMillis();

            List<ContentCrawlDTO> naverSeriesNovels = naverSeriesCrawler.crawl(driver);
            List<ContentCrawlDTO> kakaoPageNovels = kakaoPageCrawler.crawl(driver);

            long endTime = System.currentTimeMillis();

            System.out.println("크롤링 개수: " + (naverSeriesNovels.size() + kakaoPageNovels.size()) + "개");
            long elapsedTime = endTime - startTime; // 실행 시간 (ms)
            long minutes = elapsedTime / 60000; // 분 단위
            long seconds = (elapsedTime % 60000) / 1000; // 초 단위

            System.out.println("크롤링 실행 시간: " + minutes + "분 " + seconds + "초");

            long startSaveTime = System.currentTimeMillis();

            saveContents(naverSeriesNovels, "네이버시리즈", "novel");
            saveContents(kakaoPageNovels, "카카오페이지", "novel");

            long endSaveTime = System.currentTimeMillis();
            long elapsedTime2 = endSaveTime - startSaveTime; // 실행 시간 (ms)
            long minutes2 = elapsedTime2 / 60000; // 분 단위
            long seconds2 = (elapsedTime2 % 60000) / 1000; // 초 단위
            System.out.println("데이터 저장 시간: " + minutes2 + "분 " + seconds2 + "초");

        } finally {
            CustomWebDriverManager.closeDriver();
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
        Site site = siteRepository.findByPlatform(platform);
        content.addContentSite(contentID, isAdultContent, site);
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
    private List<ContentResponseDTO> getContentsByCategoryAndGenreAndTitle(String title, String genre, String category) {
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
            for (ContentSite site : n.getSites()) {
                String url = site.getSite().getUrlFormat() + site.getContentID();
                LinkDTO linkDTO = new LinkDTO(site.getSite().getPlatform(), url, site.isAdult());
                content.getLinks().add(linkDTO);
            } 
            contents.add(content);
        }
        return contents;
    }

}