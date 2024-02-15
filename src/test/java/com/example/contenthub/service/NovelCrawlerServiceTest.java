package com.example.contenthub.service;

import com.example.contenthub.crawling.SiteDTO;
import com.example.contenthub.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.crawling.novel.NovelData;
import com.example.contenthub.repository.NovelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertFalse;
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


    @DisplayName("데이터베이스에 소설 저장 및 조회 테스트")
    @Test
    void saveNovelsTest() {
        NovelData novel1 = new NovelData("Title", "coverImg", "summary", "genre", Arrays.asList(new SiteDTO("Site1", "ID1")), false);

        List<NovelData> list = new ArrayList<>();
        list.add(novel1);

        novelCrawlerService.saveNovels(list);
        NovelData result = novelCrawlerService.getDataByTitle("Title");

        assertEquals("Title", result.getTitle());
        assertEquals("coverImg", result.getCoverImg());
        assertEquals("summary", result.getSummary());
        assertEquals("genre", result.getGenre());
        assertEquals(1, result.getSite().size());
        assertEquals("Site1", result.getSite().get(0).getSiteName());
        assertEquals("ID1", result.getSite().get(0).getId());

        deleteNovelTestDataByTitle("Title");
    }

    @DisplayName("같은 작품에 대해서 다른 사이트일 때 사이트 정보만 추가되는지 확인하는 테스트")
    @Test
    public void saveNovels_existingNovelTest() {
        NovelData existingNovel = new NovelData("Title", "coverImg", "summary", "genre", Arrays.asList(new SiteDTO("Site1", "ID1")), false);
        NovelData newNovel = new NovelData("Title", "coverImg", "summary", "genre", Arrays.asList(new SiteDTO("Site2", "ID2")), false);
        List<NovelData> novels = List.of(existingNovel, newNovel);
        novelCrawlerService.saveNovels(novels);
        NovelData result = novelCrawlerService.getDataByTitle("Title");
        assertEquals(2, result.getSite().size());
        deleteNovelTestDataByTitle("Title");
    }


    //테스트 후에 테스트데이터 삭제
    private void deleteNovelTestDataByTitle(String title) {
        novelCrawlerService.deleteDataByTitle(title);
    }
}
