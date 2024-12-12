package com.example.contenthub.service.crawling;

import com.example.contenthub.entity.Content;
import com.example.contenthub.service.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.service.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.dto.SiteDTO;
import com.example.contenthub.entity.NovelSite;
import com.example.contenthub.entity.Site;
import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.NovelSiteRepository;
import com.example.contenthub.repository.SiteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NovelCrawlerService {

    private final ContentRepository contentRepository;
    private final SiteRepository siteRepository;
    private final NovelSiteRepository novelSiteRepository;
    private final NaverSeriesCrawler naverSeriesCrawler;
    private final KakaoPageCrawler kakaoPageCrawler;

    public void crawl() throws IOException {
        List<ContentDTO> naverSeriesNovels = naverSeriesCrawler.crawl();
        List<ContentDTO> kakaoPageNovels = kakaoPageCrawler.crawl();

        saveContents(naverSeriesNovels, "네이버시리즈");
        saveContents(kakaoPageNovels, "카카오페이지");
    }

    @Transactional
    public void saveContents(List<ContentDTO> contents, String siteName) {
        for (ContentDTO content : contents) {
            Content existingNovel = getDataByTitle(content.getTitle());

            if (existingNovel == null) { // 작품이 없는 경우에만 작품추가
                Content novel = new Content(
                        content.getTitle(),
                        content.getCoverImg(),
                        content.getSummary(),
                        content.getGenre(),
                        content.isAdultContent());
                saveNovelSite(novel, siteName, content.getProductId());
                contentRepository.save(novel);
            } else { // 작품이 이미 존재하는 경우엔 사이트 추가만
                saveNovelSite(existingNovel, siteName, content.getProductId());
                contentRepository.save(existingNovel); // 작품 정보 업데이트
            }

        }
    }

    public boolean isDataExist(String title, String site) {
        Content novelData = contentRepository.findByTitle(title);
        return novelData != null && novelData.hasSite(site);
    }

    public List<ContentDTO> getContentDTOList(List<Content> list) {
        List<ContentDTO> contents = new ArrayList<>();
        for (Content n : list) {
            ContentDTO content = new ContentDTO(n.getTitle(), n.getCoverImg(), n.getSummary(), n.getGenre(),
                    n.isAdultContent(), new ArrayList<>());
            for (NovelSite site : n.getSites()) {
                SiteDTO siteDTO = new SiteDTO(site.getSite().getName(), site.getProductId());
                content.getSiteDTOs().add(siteDTO);
            }
            contents.add(content);
        }
        return contents;
    }

    public List<ContentDTO> getAllData() {
        List<Content> list = contentRepository.findAll();
        return getContentDTOList(list);
    }

    public List<ContentDTO> getDataByGenre(String genre) {
        List<Content> list = contentRepository.findByGenre(genre);
        return getContentDTOList(list);
    }

    public Content getDataByTitle(String title) {
        return contentRepository.findByTitle(title);
    }

    public List<ContentDTO> getDataByTitleContaining(String title) {
        List<Content> list = contentRepository.findByTitleContaining(title);
        return getContentDTOList(list);
    }

    @Transactional
    public void deleteDataByTitle(String title) {
        contentRepository.deleteByTitle(title);
    }

    public void saveNovelSite(Novel novel, String siteName, String productId) {
        Site site = siteRepository.findByName(siteName);
        NovelSite newNovelSite = new NovelSite();
        newNovelSite.setNovel(novel);
        newNovelSite.setSite(site);
        newNovelSite.setProductId(productId);
        novel.getSites().add(newNovelSite); // 작품에 새로운 사이트 정보 추가
        novelSiteRepository.save(newNovelSite);
    }
}