package com.example.contenthub;

import com.example.contenthub.crawling.NovelCrawler;

import java.io.IOException;
import java.util.List;

import com.example.contenthub.crawling.NovelData;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HubController {
    @GetMapping("/novel")
    public List<NovelData> test() throws IOException {
        return NovelCrawler.crawl();
    }
}