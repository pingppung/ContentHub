package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;

import com.example.contenthub.exception.CrawlerException;
import com.example.contenthub.login.NaverSeriesLogin;
import com.example.contenthub.service.NovelCrawlerService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.contenthub.crawling.WebDriverUtils.*;

@Component
public class NaverSeriesCrawler {
    @Autowired
    NaverSeriesLogin naverLogin;

    @Autowired
    @Lazy
    NovelCrawlerService novelCrawlerService;

    final static String BASE_URL = "https://series.naver.com/novel/top100List.series?";
    final static String TYPECODEPARAM = "rankingTypeCode=HOURLY";
    final static String CATEGORYCODEPARAM = "categoryCode=ALL";
    final static String PAGEPARAM = "page=";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        WebDriver detailDriver = createWebDriver();
        try {
            setUpDrivers(driver, detailDriver);

            for (int i = 1; i < 6; i++) {
                navigateToPage(driver, i);

                List<NovelData> list = getNaverSeriesData(driver, detailDriver);
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

    private List<NovelData> getNaverSeriesData(WebDriver driver, WebDriver detailDriver) throws InterruptedException {
        List<NovelData> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"content\"]/div/ul/li"));
        for (WebElement element : novelElements) {
            NovelData novelData = extractNovelData(element, detailDriver);
            if (novelData != null) novels.add(novelData);
        }
        return novels;
    }

    private NovelData extractNovelData(WebElement element, WebDriver detailDriver) {
        WebElement novel = element.findElement(By.xpath(".//div[2]/h3/a"));
        String title = extractTitle(novel.getText());
        if (novelCrawlerService.isDataExist(title, NovelSite.NAVER_SERIES.getName())) return null;

        String detailHref = novel.getAttribute("href");
        String productId = extractProductId(detailHref);
        boolean adultContent = isAdultContent(element);
        WebElement coverImgElement = element.findElement(By.xpath(".//a/img"));
        String coverImg = coverImgElement.getAttribute("src");
        String summary = element.findElement(By.xpath(".//div[2]/p[2]")).getText();
        String genre = getNovelGenre(detailDriver, detailHref, adultContent);

        return new NovelData(title, coverImg, summary, genre,
                Arrays.asList(new SiteDTO(NovelSite.NAVER_SERIES.getName(), productId)), adultContent);
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
