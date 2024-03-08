package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;
import com.example.contenthub.login.KakaoPageLogin;
import com.example.contenthub.login.NaverSeriesLogin;
import com.example.contenthub.service.NovelCrawlerService;
import com.google.gson.Gson;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.contenthub.crawling.WebDriverUtils.*;

@Component
public class KakaoPageCrawler {
    @Autowired
    KakaoPageLogin kakakoLogin;

    @Autowired
    @Lazy
    NovelCrawlerService novelCrawlerService;
    final static String BASE_URL = "https://page.kakao.com";
    final static String TOP300 = "/menu/10011/screen/94";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        WebDriver detailDriver = createWebDriver();
        try {
            String url = BASE_URL + TOP300;
            driver.get(url);
            detailDriver.get(url);
            kakakoLogin.activateBot(driver); //일단 커버이미지 때문에 로그인 상태로 만들어놓음
            kakakoLogin.activateBot(detailDriver);

            scrollPageToBottom(driver);


            List<NovelData> list = getKakaoPageData(driver, detailDriver);
            novels.addAll(list);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeWebDriver(driver);
            closeWebDriver(detailDriver);
        }


        return novels;
    }

    private List<NovelData> getKakaoPageData(WebDriver driver, WebDriver detailDriver) {
        List<NovelData> novels = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> novelElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div")));
        //List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div"));
        for (WebElement element : novelElements) {

            WebElement novel = element.findElement(By.xpath(".//div/a/div"));
            String jsonString = novel.getAttribute("data-t-obj");
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonString, Map.class);
            Map<String, String> eventMeta = (Map<String, String>) data.get("eventMeta");

            String productId = eventMeta.get("id");
            String title = eventMeta.get("name");

            System.out.println(title);
            if (novelCrawlerService.isDataExist(title, NovelSite.KAKAO_PAGE.getName())) continue;

            String genre = eventMeta.get("subcategory");

            WebElement imgDiv = novel.findElement(By.xpath(".//div/div/img"));
            String coverImg = imgDiv.getAttribute("src");

            boolean adultContent = isAdultContent(element);
            String summary = getNovelSummary(detailDriver, productId);

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
            Thread.sleep(2000);
            Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
            if (newHeight.equals(lastHeight)) {
                break;
            }
        }
    }

    private String getNovelSummary(WebDriver driver, String productId) {

        try {
            String detailURL = String.format("%s/content/%s?tab_type=about", BASE_URL, productId);
            driver.get(detailURL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By summaryLocator = By.xpath("//*[@id=\"__next\"]/div/div[2]/div[1]/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span");
            WebElement summary = wait.until(ExpectedConditions.visibilityOfElementLocated(summaryLocator));

//            //By summaryLocator = By.xpath("");
//            waitForElement(driver, summaryLocator);
//            WebElement summary = driver.findElement(summaryLocator);

            return summary.getText();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
