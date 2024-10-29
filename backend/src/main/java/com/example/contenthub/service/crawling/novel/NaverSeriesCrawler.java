package com.example.contenthub.service.crawling.novel;

import com.example.contenthub.dto.ContentDTO;
import com.example.contenthub.exception.CrawlerException;
import com.example.contenthub.service.auth.social.NaverSeriesLogin;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.contenthub.service.crawling.WebDriverUtils.closeWebDriver;
import static com.example.contenthub.service.crawling.WebDriverUtils.createWebDriver;

@Component
@RequiredArgsConstructor
public class NaverSeriesCrawler {

    private static final String BASE_URL = "https://series.naver.com/novel/top100List.series?";
    private static final String TYPECODEPARAM = "rankingTypeCode=HOURLY";
    private static final String CATEGORYCODEPARAM = "categoryCode=ALL";
    private static final String PAGEPARAM = "page=";

    private final NaverSeriesLogin naverLogin;

    public List<ContentDTO> crawl() {
        List<ContentDTO> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        WebDriver detailDriver = createWebDriver();
        try {
            setUpDrivers(driver, detailDriver);

            for (int i = 1; i < 6; i++) {
                navigateToPage(driver, i);

                List<ContentDTO> list = getNaverSeriesData(driver, detailDriver);
                novels.addAll(list);
            }
        } catch (TimeoutException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.TIMEOUT_EXCEPTION.getMessage(), e);
        } catch (InterruptedException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.UNEXPECTED_EXCEPTION.getMessage(), e);
        } finally {
            closeWebDriver(driver);
            closeWebDriver(detailDriver);
        }
        return novels;
    }

    private void setUpDrivers(WebDriver driver, WebDriver detailDriver) {
        driver.get(BASE_URL);
        detailDriver.get(BASE_URL + TYPECODEPARAM + "&" + CATEGORYCODEPARAM);
        naverLogin.activateBot(driver);
        naverLogin.activateBot(detailDriver);
    }

    private void navigateToPage(WebDriver driver, int page) {
        String url = BASE_URL + TYPECODEPARAM + "&" + CATEGORYCODEPARAM + "&" + PAGEPARAM + page;
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(url));
    }

    private List<ContentDTO> getNaverSeriesData(WebDriver driver, WebDriver detailDriver) throws InterruptedException {
        List<ContentDTO> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"content\"]/div/ul/li"));
        for (WebElement element : novelElements) {
            ContentDTO contentData = extractNovelData(element, detailDriver);
            if (contentData != null)
                novels.add(contentData);
        }
        return novels;
    }

    private ContentDTO extractNovelData(WebElement element, WebDriver detailDriver) {
        WebElement novel = element.findElement(By.xpath(".//div[2]/h3/a"));
        String title = extractTitle(novel.getText());
//        if (novelCrawlerService.isDataExist(title, SiteType.NAVER_SERIES.getName()))
//            return null;

        String detailHref = novel.getAttribute("href");
        String productId = extractProductId(detailHref);
        boolean adultContent = isAdultContent(element);
        WebElement coverImgElement = element.findElement(By.xpath(".//a/img"));
        String coverImg = coverImgElement.getAttribute("src");
        String summary = element.findElement(By.xpath(".//div[2]/p[2]")).getText();
        String genre = getNovelGenre(detailDriver, detailHref, adultContent);

        return new ContentDTO(title, coverImg, summary, genre, adultContent, productId);
    }

    private String getNovelGenre(WebDriver driver, String href, boolean isAdult) {
        try {
            String detailURL = href;

            driver.get(detailURL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By genreLocator = By.xpath("//*[@id=\"content\"]/ul[1]/li/ul/li[2]/span/a");
            WebElement genreElement = wait.until(ExpectedConditions.presenceOfElementLocated(genreLocator));

            return genreElement.getText();
        } catch (TimeoutException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.TIMEOUT_EXCEPTION.getMessage(), e);
        }
    }

    private boolean isAdultContent(WebElement element) {
        try {
            element.findElement(By.xpath(".//div[2]/h3/em[@class='ico n19']"));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    // 정규표현식을 사용하여 [ ... ] 패턴을 찾아내고 해당 부분을 제거
    private static String extractTitle(String title) {
        return title.replaceAll("\\[.*?\\]", "").trim();
    }

    private String extractProductId(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            System.out.println("해당 작품 id를 찾을 수가 없습니다!");
            return null;
        }
    }
}
