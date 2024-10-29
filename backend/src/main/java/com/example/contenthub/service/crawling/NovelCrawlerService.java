package com.example.contenthub.service.crawling;

import com.example.contenthub.service.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.service.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.dto.SiteDTO;
import com.example.contenthub.entity.Novel;
import com.example.contenthub.entity.NovelSite;
import com.example.contenthub.entity.Site;
import com.example.contenthub.repository.NovelRepository;
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

    private final NovelRepository novelRepository;
    private final SiteRepository siteRepository;
    private final NovelSiteRepository novelSiteRepository;
    private final NaverSeriesCrawler naverSeriesCrawler;
    private final KakaoPageCrawler kakaoPageCrawler;

    public void crawl() throws IOException {
        List<ContentDTO> naverSeriesNovels = naverSeriesCrawler.crawl();
        List<ContentDTO> kakaoPageNovels = kakaoPageCrawler.crawl();

        saveNovels(naverSeriesNovels, "네이버시리즈");
        saveNovels(kakaoPageNovels, "카카오페이지");
    }

    @Transactional
    public void saveNovels(List<ContentDTO> contents, String siteName) {

        for (ContentDTO content : contents) {
            Novel existingNovel = getDataByTitle(content.getTitle());
            // 작품이 없는 경우에만 작품추가
            if (existingNovel == null) {
                Novel novel = new Novel(content.getTitle(), content.getCoverImg(), content.getSummary(),
                        content.getGenre(), content.isAdultContent());
                saveNovelSite(novel, siteName, content.getProductId());
                novelRepository.save(novel);
            } else { // 작품이 이미 존재하는 경우엔 사이트 추가만
                saveNovelSite(existingNovel, siteName, content.getProductId());
                novelRepository.save(existingNovel); // 작품 정보 업데이트
            }

        }
    }

    public boolean isDataExist(String title, String site) {
        Novel novelData = novelRepository.findByTitle(title);
        return novelData != null && novelData.hasSite(site);
    }

    public List<ContentDTO> getContentDTOList(List<Novel> list) {
        List<ContentDTO> contents = new ArrayList<>();
        for (Novel n : list) {
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
        List<Novel> list = novelRepository.findAll();
        return getContentDTOList(list);
    }

    public List<ContentDTO> getDataByGenre(String genre) {
        List<Novel> list = novelRepository.findByGenre(genre);
        return getContentDTOList(list);
    }

    public Novel getDataByTitle(String title) {
        return novelRepository.findByTitle(title);
    }

    public List<ContentDTO> getDataByTitleContaining(String title) {
        List<Novel> list = novelRepository.findByTitleContaining(title);
        return getContentDTOList(list);
    }

    @Transactional
    public void deleteDataByTitle(String title) {
        novelRepository.deleteByTitle(title);
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