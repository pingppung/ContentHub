package com.example.contenthub;

import com.example.contenthub.crawling.CrawlingTest;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HubController {
    @GetMapping("/api/series")
    public List<String> test() throws IOException {
        return CrawlingTest.crawl();
    }
}