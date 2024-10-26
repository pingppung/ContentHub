package com.example.contenthub.service;

import com.example.contenthub.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.entity.Novel;
import com.example.contenthub.entity.NovelSite;
import com.example.contenthub.repository.NovelRepository;
import com.example.contenthub.repository.NovelSiteRepository;
import com.example.contenthub.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NovelCrawlerServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private NovelSiteRepository novelSiteRepository;

    @Mock
    private NaverSeriesCrawler naverSeriesCrawler;

    @Mock
    private KakaoPageCrawler kakaoPageCrawler;

    @InjectMocks
    private NovelCrawlerService novelCrawlerService;

    private ContentDTO content;
    private Novel mockNovel;

    @BeforeEach
    void setUp() {
        // 공통 데이터 설정
        content = new ContentDTO("Title", "coverImg", "summary", "genre", false, "테스트ID");
        mockNovel = new Novel(content.getTitle(), content.getCoverImg(), content.getSummary(),
                content.getGenre(), content.isAdultContent());
    }

    @DisplayName("소설 저장 테스트")
    @Test
    public void saveNovelsTest() {
        List<ContentDTO> list = new ArrayList<>();
        list.add(content);

        // Given: 초기 상태에서는 소설이 존재하지 않음
        when(novelRepository.findByTitle("Title")).thenReturn(null);

        // When: 소설을 저장
        novelCrawlerService.saveNovels(list, "네이버시리즈");

        // Then: 소설이 저장되었는지 확인
        verify(novelRepository, times(1)).save(any(Novel.class));
    }

    @DisplayName("소설 조회 테스트")
    @Test
    void getDataByTitleTest() {
        // Given: 소설이 존재한다고 가정
        when(novelRepository.findByTitle("Title")).thenReturn(mockNovel);

        // When: 소설을 조회
        Novel result = novelCrawlerService.getDataByTitle("Title");

        // Then: 조회된 소설이 mockNovel과 동일한지 확인
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("coverImg", result.getCoverImg());
        assertEquals("summary", result.getSummary());
        assertEquals("genre", result.getGenre());
        assertFalse(result.isAdultContent());
    }

    @DisplayName("전체 데이터 조회 테스트")
    @Test
    void getAllDataTest() {
        ContentDTO content2 = new ContentDTO("Title2", "coverImg", "summary", "genre", false, "테스트ID");
        Novel mockNovel2 = new Novel(content.getTitle(), content.getCoverImg(), content.getSummary(),
                content.getGenre(), content.isAdultContent());

        // Given: 데이터베이스에 소설이 존재한다고 가정
        when(novelRepository.findAll()).thenReturn(List.of(mockNovel, mockNovel2));

        // When: 전체 데이터 조회
        List<ContentDTO> result = novelCrawlerService.getAllData();

        // Then: 데이터 크기와 내용 확인
        assertEquals(2, result.size());
        assertEquals("Title", result.get(0).getTitle());
        assertEquals("coverImg", result.get(0).getCoverImg());
        assertEquals("summary", result.get(0).getSummary());
        assertEquals("genre", result.get(0).getGenre());
        assertFalse(result.get(0).isAdultContent());
    }

//    @DisplayName("같은 작품에 대해서 다른 사이트일 때 사이트 정보만 추가되는지 확인하는 테스트")
//    @Test
//    public void saveNovels_existingNovelTest() {
//        when(novelRepository.findByTitle("Title")).thenReturn(mockNovel);
//        when(novelRepository.save(any(Novel.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        novelCrawlerService.saveNovels(List.of(content), "네이버시리즈");
//
//        ContentDTO newNovel = new ContentDTO("Title", "coverImg", "summary", "genre", false, "ID2");
//        novelCrawlerService.saveNovels(List.of(newNovel), "카카오페이지");
//
//        Novel result = novelCrawlerService.getDataByTitle("Title");
//        assertNotNull(result);
//        assertEquals(2, result.getSites().size());
//
//        // 추가 검증: 두 개의 사이트 정보가 제대로 저장되었는지 확인
//        List<NovelSite> sites = result.getSites();
//        assertTrue(sites.stream().anyMatch(site -> "네이버시리즈".equals(site.getSite())));
//        assertTrue(sites.stream().anyMatch(site -> "카카오페이지".equals(site.getSite())));
//    }
}
