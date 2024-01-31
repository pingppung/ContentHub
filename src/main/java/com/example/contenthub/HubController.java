package com.example.contenthub;

import com.example.contenthub.service.NovelCrawlerService;

import java.io.IOException;
import java.util.List;

import com.example.contenthub.crawling.NovelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HubController {
    private final NovelCrawlerService novelCrawlerService;

    @Autowired
    public HubController(NovelCrawlerService novelCrawlerService) {
        this.novelCrawlerService = novelCrawlerService;
    }

    @GetMapping("/novel")
    public List<NovelData> test() throws IOException {
        novelCrawlerService.crawl();
        return novelCrawlerService.getAllData();
    }
}