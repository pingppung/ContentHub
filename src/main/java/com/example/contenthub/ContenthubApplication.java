package com.example.contenthub;

import com.example.contenthub.crawling.CrawlingTest;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContenthubApplication {

	public static void main(String[] args) throws IOException {

		//SpringApplication.run(ContenthubApplication.class, args);
		CrawlingTest crawler = new CrawlingTest();
		try {
			crawler.crawl();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
