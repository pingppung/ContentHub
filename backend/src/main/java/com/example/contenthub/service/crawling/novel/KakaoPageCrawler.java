package com.example.contenthub.service.crawling.novel;

import com.example.contenthub.constants.SiteType;
import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.exception.CrawlerException;
import com.example.contenthub.service.auth.social.KakaoPageLogin;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoPageCrawler {
    private static final String BASE_URL = "https://page.kakao.com/menu/10011/screen/94";

    private final KakaoPageLogin kakaoLogin;

    private WebDriver mainDriver;
    private WebDriver detailDriver;

    public List<ContentDTO> crawl(WebDriver mainDriver, WebDriver detailDriver) {
        List<ContentDTO> novels = new ArrayList<>();
        try {
            // 필요한 WebDriver 초기 설정
            setUpDrivers(mainDriver, detailDriver);

            scrollPageToBottom(mainDriver);
            List<ContentDTO> list = getKakaoPageData(mainDriver, detailDriver);
            novels.addAll(list);
        } catch (TimeoutException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.TIMEOUT_EXCEPTION.getMessage(), e);
        } catch (InterruptedException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.UNEXPECTED_EXCEPTION.getMessage(), e);
        }
        return novels;
    }

    private void setUpDrivers(WebDriver mainDriver, WebDriver detailDriver) {
        // WebDriver를 클래스 필드에 할당하여 사용
        this.mainDriver = mainDriver;
        this.detailDriver = detailDriver;

        // 페이지 열기 및 로그인
        mainDriver.get(BASE_URL);
        detailDriver.get(BASE_URL);

        kakaoLogin.activateBot(mainDriver);
        kakaoLogin.activateBot(detailDriver);
    }

    private void scrollPageToBottom(WebDriver driver) throws InterruptedException {
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

    private List<ContentDTO> getKakaoPageData(WebDriver driver, WebDriver detailDriver) {
        List<ContentDTO> novels = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> novelElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div")));
        for (WebElement element : novelElements) {
            ContentDTO contentData = extractNovelData(element, detailDriver);
            if (contentData != null)
                novels.add(contentData);
        }
        return novels;
    }

    private ContentDTO extractNovelData(WebElement element, WebDriver detailDriver) {
        WebElement novel = element.findElement(By.xpath(".//div/a/div"));
        String jsonString = novel.getAttribute("data-t-obj");
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(jsonString, Map.class);
        Map<String, String> eventMeta = (Map<String, String>) data.get("eventMeta");

        String contentId = eventMeta.get("id");
        String title = eventMeta.get("name");

        String genre = eventMeta.get("subcategory");
        WebElement imgDiv = novel.findElement(By.xpath(".//div/div/img"));
        String coverImg = imgDiv.getAttribute("src");
        boolean adultContent = isAdultContent(element);
        String summary = getNovelSummary(detailDriver, contentId);

        return new ContentDTO(title, coverImg, summary, genre, adultContent, SiteType.KAKAO_PAGE, contentId);
    }

    private boolean isAdultContent(WebElement element) {
        try {
            element.findElement(By.xpath(".//img[@alt='19세 뱃지']"));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private String getNovelSummary(WebDriver driver, String productId) {
        try {
            String detailURL = String.format("%s/content/%s?tab_type=about", "https://page.kakao.com", productId);
            driver.get(detailURL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By summaryLocator = By.xpath(
                    "//*[@id=\"__next\"]/div/div[2]/div[1]/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span");
            WebElement summary = wait.until(ExpectedConditions.visibilityOfElementLocated(summaryLocator));

            return summary.getText();
        } catch (TimeoutException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.TIMEOUT_EXCEPTION.getMessage(), e);
        }
    }
}
