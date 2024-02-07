package com.example.contenthub.service;

import com.example.contenthub.crawling.Site;
import com.example.contenthub.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.crawling.novel.NovelData;
import com.example.contenthub.repository.NovelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class NovelCrawlerServiceTest {

    @Autowired
    private NovelRepository novelRepository;

    private NovelCrawlerService novelCrawlerService;

    @BeforeEach
    public void setUp() {
        novelCrawlerService = new NovelCrawlerService(novelRepository);
    }

    @Test
    public void testSaveNovels() {
        NovelData existingNovel = new NovelData("Title", "coverImg", "summary", "genre", Arrays.asList(new Site("Site1", "ID1")));
        NovelData newNovel = new NovelData("Title", "coverImg", "summary", "genre", Arrays.asList(new Site("Site2", "ID2")));
        List<NovelData> list = new ArrayList<>();
        list.add(existingNovel);
        list.add(newNovel);
        novelCrawlerService.saveNovels(list);

        NovelData result = novelCrawlerService.getDataByTitle("Title");
        assertEquals(2, result.getSite().size());

        //테스트 후에 테스트데이터 삭제
        novelCrawlerService.deleteDataByTitle("Title");
    }
}
