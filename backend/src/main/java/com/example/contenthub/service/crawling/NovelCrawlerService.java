package com.example.contenthub.service.crawling;

import com.example.contenthub.entity.Novel;
import com.example.contenthub.entity.NovelLink;
import com.example.contenthub.repository.NovelRepository;
import com.example.contenthub.service.crawling.novel.KakaoPageCrawler;
import com.example.contenthub.service.crawling.novel.NaverSeriesCrawler;
import com.example.contenthub.dto.ContentDTO;
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
public class NovelCrawlerService {

    private final NovelRepository novelRepository;
    private final NaverSeriesCrawler naverSeriesCrawler;
    private final KakaoPageCrawler kakaoPageCrawler;

    public void crawl() throws IOException {
        WebDriver[] drivers = CustomWebDriverManager.getDriver();
        WebDriver mainDriver = drivers[0];  // 첫 번째 드라이버
        WebDriver detailDriver = drivers[1];  // 두 번째 드라이버

        try {
            List<ContentDTO> naverSeriesNovels = naverSeriesCrawler.crawl(mainDriver, detailDriver);
            //List<ContentDTO> kakaoPageNovels = kakaoPageCrawler.crawl(mainDriver, detailDriver);

            saveContents(naverSeriesNovels);
            //saveContents(kakaoPageNovels);
        } finally {
            CustomWebDriverManager.closeDriver();
        }
    }

    @Transactional
    public void saveContents(List<ContentDTO> contents) {
        for (ContentDTO content : contents) {
            Novel existingNovel = getDataByTitle(content.getTitle());

            if (existingNovel == null) { // 작품이 없는 경우에만 작품 추가
                Novel novel = new Novel(
                        content.getTitle(),
                        content.getSummary(),
                        content.getCoverImg(),
                        content.isAdultContent(),
                        content.getGenre());
                saveNovelSite(novel, content.getLink());
                novelRepository.save(novel);  // Novel만 저장
            } else { // 작품이 이미 존재하는 경우엔 사이트 추가만
                saveNovelSite(existingNovel, content.getLink());
                novelRepository.save(existingNovel);  // 작품 정보 업데이트
            }
        }
    }

    public void saveNovelSite(Novel novel, LinkDTO linkdto) {
        // Link newLink = Link.builder()
        //         .url(linkdto.getUrl())
        //         .siteName(linkdto.getSiteName())
        //         .content(novel)  // Novel 객체와 링크를 연결
        //         .build();
        // novel.getLinks().add(newLink);  // Novel에 새로운 Link 추가
        // linkRepository.save(newLink);  // 필요시 linkRepository로 링크 저장
        System.out.println(linkdto.getContentId());
        NovelLink newLink = new NovelLink(linkdto.getUrl(), linkdto.getSiteName(), linkdto.getContentId(), novel);
        novel.getLinks().add(newLink);
    }


    //넘겨주기 위해서 db에서 찾은 후 dto로 변환시키는 메서드
    // Novel 리스트를 받아 ContentDTO 리스트로 변환
    public List<ContentDTO> getContentDTOList(List<Novel> list) {
        List<ContentDTO> contents = new ArrayList<>();
        for (Novel n : list) {
            ContentDTO content = new ContentDTO(
                    n.getTitle(),
                    n.getImageUrl(),
                    n.getDescription(),
                    n.getGenre(),
                    n.isAdultContent(),
                    new ArrayList<>());
            for (NovelLink link : n.getLinks()) {
                LinkDTO linkDTO = new LinkDTO(link.getSiteName(), link.getContentUId(), link.getUrl());
                content.getLinks().add(linkDTO);
            }
            contents.add(content);
        }
        return contents;
    }

    public List<ContentDTO> getAllData() {
        List<Novel> list = novelRepository.findAll();  // Novel만 조회
        return getContentDTOList(list);
    }

    public List<ContentDTO> getDataByGenre(String genre) {
        List<Novel> list = novelRepository.findByGenre(genre);  // Genre별로 Novel 조회
        return getContentDTOList(list);
    }

    public Novel getDataByTitle(String title) {
        return novelRepository.findByTitle(title);  // 제목으로 Novel 조회
    }

    public List<ContentDTO> getDataByTitleContaining(String title) {
        List<Novel> list = novelRepository.findByTitleContaining(title);  // 제목에 포함된 내용을 가진 Novel 조회
        return getContentDTOList(list);
    }

    @Transactional
    public void deleteDataByTitle(String title) {
        novelRepository.deleteByTitle(title);  // 제목으로 Novel 삭제
    }


}