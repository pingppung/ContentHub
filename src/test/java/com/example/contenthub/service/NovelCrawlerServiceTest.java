package com.example.contenthub.service;

import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.entity.Novel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NovelCrawlerServiceTest {

    @Autowired
    private NovelCrawlerService novelCrawlerService;

    @DisplayName("데이터베이스에 소설 저장 및 조회 테스트")
    @Test
    void saveNovelsTest() {
        ContentDTO content = new ContentDTO("Title", "coverImg", "summary", "genre", false, "테스트ID");

        List<ContentDTO> list = new ArrayList<>();
        list.add(content);

        novelCrawlerService.saveNovels(list, "네이버시리즈");
        Novel result = novelCrawlerService.getDataByTitle("Title");

        assertEquals("Title", result.getTitle());
        assertEquals("coverImg", result.getCoverImg());
        assertEquals("summary", result.getSummary());
        assertEquals("genre", result.getGenre());
        assertEquals(1, result.getSites().size());
        assertEquals("네이버시리즈", result.getSites().get(0).getSite().getName());
        assertEquals("테스트ID", result.getSites().get(0).getProductId());
        deleteNovelTestDataByTitle("Title");
    }

    @DisplayName("같은 작품에 대해서 다른 사이트일 때 사이트 정보만 추가되는지 확인하는 테스트")
    @Test
    public void saveNovels_existingNovelTest() {
        ContentDTO existingNovel = new ContentDTO("Title", "coverImg", "summary", "genre", false, "ID1");
        ContentDTO newNovel = new ContentDTO("Title", "coverImg", "summary", "genre", false, "ID2");
        novelCrawlerService.saveNovels(List.of(existingNovel), "네이버시리즈");
        novelCrawlerService.saveNovels(List.of(newNovel), "카카오페이지");
        Novel result = novelCrawlerService.getDataByTitle("Title");
        assertEquals(2, result.getSites().size());
        deleteNovelTestDataByTitle("Title");
    }


    //테스트 후에 테스트데이터 삭제
    private void deleteNovelTestDataByTitle(String title) {
        novelCrawlerService.deleteDataByTitle(title);
    }
}
