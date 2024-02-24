package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;
import com.google.gson.Gson;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.contenthub.crawling.WebDriverUtils.*;

@Component
public class KakaoPageCrawler {
    final static String BASE_URL = "https://page.kakao.com";
    final static String TOP300 = "/menu/10011/screen/94";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        try {
            String url = BASE_URL + TOP300;
            driver.get(url);

            scrollPageToBottom(driver);

            List<NovelData> list = getKakaoPageData(driver);
            novels.addAll(list);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeWebDriver(driver);
        }


        return novels;
    }

    private List<NovelData> getKakaoPageData(WebDriver driver) {
        List<NovelData> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div"));
        for (WebElement element : novelElements) {

            WebElement novel = element.findElement(By.xpath(".//div/a/div"));
            String jsonString = novel.getAttribute("data-t-obj");
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonString, Map.class);
            Map<String, String> eventMeta = (Map<String, String>) data.get("eventMeta");

            String productId = eventMeta.get("id");
            String title = eventMeta.get("name");
            String genre = eventMeta.get("subcategory");

            WebElement imgDiv = novel.findElement(By.xpath(".//div/div/img"));
            String coverImg = imgDiv.getAttribute("src");

            boolean adultContent = isAdultContent(element);

            String summary = getNovelSummary(productId);

            NovelData novelData = new NovelData(title, coverImg, summary, genre, Arrays.asList(new SiteDTO(NovelSite.KAKAO_PAGE.getName(), productId)), adultContent);
            novels.add(novelData);
        }
        return novels;
    }

    private boolean isAdultContent(WebElement element) {
        try {
            element.findElement(By.xpath(".//img[@alt='19세 뱃지']"));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private void scrollPageToBottom(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (true) {
            Long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            wait.until(webDriver -> (Long) js.executeScript("return document.body.scrollHeight") != lastHeight);
        }
    }

    private String getNovelSummary(String productId) {
        WebDriver driver = createWebDriver();
        try {
            String detailURL = String.format("%s/content/%s?tab_type=about", BASE_URL, productId);
            driver.get(detailURL);

            By summaryLocator = By.xpath("//*[@id=\"__next\"]/div/div[2]/div[1]/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span");
            waitForElement(driver, summaryLocator);
            WebElement summary = driver.findElement(summaryLocator);

            return summary.getText();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
