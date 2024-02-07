package com.example.contenthub.service;

import java.io.IOException;
import java.util.List;

import com.example.contenthub.crawling.Site;
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

    public void crawl() throws IOException {
        List<NovelData> naverSeriesNovels = naverSeriesCrawler.crawl();
        //List<NovelData> ridiBooksNovels = ridiBooksCrawler.crawl();

        saveNovels(naverSeriesNovels);
        //saveNovels(ridiBooksNovels);
    }

    public void saveNovels(List<NovelData> novels) {
        for (NovelData novel : novels) {

            NovelData existingNovel = novelRepository.findByTitle(novel.getTitle());
            // 이미 존재하는 title에 대해서 site 값을 추가
            if (existingNovel != null) {
                System.out.println(novel.getSite().get(0));
                List<Site> updatedSites = existingNovel.getSite();
                //System.out.println(novel.getSites().get(0));
                updatedSites.add(novel.getSite().get(0));
                existingNovel.setSite(updatedSites);
                novelRepository.save(existingNovel);
            } else {
                novelRepository.save(novel);
            }
        }
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
}