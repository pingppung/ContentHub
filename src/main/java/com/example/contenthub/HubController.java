package com.example.contenthub;

import com.example.contenthub.service.NovelCrawlerService;

import java.io.IOException;
import java.util.List;

import com.example.contenthub.crawling.NovelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HubController {
    private final NovelCrawlerService novelCrawlerService;

    @Autowired
    public HubController(NovelCrawlerService novelCrawlerService) {
        this.novelCrawlerService = novelCrawlerService;
    }

    @GetMapping("/novel/{genre}")
    public List<NovelData> test2(@PathVariable String genre) {
        System.out.println(genre);
        if (genre.equals("a")) return novelCrawlerService.getAllData();
        else return novelCrawlerService.getDataByGenre(genre);

    }
    // 해당 위치에 들어갈때마다 크롤링으로 인한 속도 문제로 빠른 테스트 진행을 위해 잠깐 주석처리해놓음
//    @GetMapping("/")
//    public void crawl() throws IOException {
//        novelCrawlerService.crawl();
//    }


    @GetMapping("/novel")
    public List<NovelData> test() {
        return novelCrawlerService.getAllData();
    }


}