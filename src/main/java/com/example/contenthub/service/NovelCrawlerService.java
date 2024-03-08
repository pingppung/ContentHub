package com.example.contenthub.service;

import java.io.IOException;
import java.util.List;

import com.example.contenthub.crawling.SiteDTO;
import com.example.contenthub.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.crawling.novel.NovelData;
import com.example.contenthub.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NovelCrawlerService {

    private final NovelRepository novelRepository;

    @Autowired
    public NovelCrawlerService(NovelRepository novelRepository) {
        this.novelRepository = novelRepository;
    }

    @Autowired
    private NaverSeriesCrawler naverSeriesCrawler;

    @Autowired
    private KakaoPageCrawler kakaoPageCrawler;

    public void crawl() throws IOException {
        //List<NovelData> naverSeriesNovels = naverSeriesCrawler.crawl();
        List<NovelData> kakaoPageNovels = kakaoPageCrawler.crawl();

        //saveNovels(naverSeriesNovels);
        saveNovels(kakaoPageNovels);
    }

    public void saveNovels(List<NovelData> novels) {
        for (NovelData novel : novels) {

            NovelData existingNovel = novelRepository.findByTitle(novel.getTitle());
            // 이미 존재하는 title에 대해서 site 값을 추가
            if (existingNovel != null) {
                List<SiteDTO> updatedSites = existingNovel.getSite();
                updatedSites.add(novel.getSite().get(0));
                existingNovel.setSite(updatedSites);
                novelRepository.save(existingNovel);
            } else {
                novelRepository.save(novel);
            }
        }
    }

    public boolean isDataExist(String title, String site) {
        NovelData novelData = novelRepository.findByTitle(title);
        return novelData != null && novelData.hasSite(site);
    }

    public List<NovelData> getAllData() {
        return novelRepository.findAll();
    }

    public List<NovelData> getDataByGenre(String genre) {
        return novelRepository.findByGenre(genre);
    }

    public NovelData getDataByTitle(String title) {
        return novelRepository.findByTitle(title);
    }

    public void deleteDataByTitle(String title) {
        novelRepository.deleteByTitle(title);
    }

    public List<NovelData> getDataByTitleContaining(String title) {
        return novelRepository.findByTitleContaining(title);
    }
}