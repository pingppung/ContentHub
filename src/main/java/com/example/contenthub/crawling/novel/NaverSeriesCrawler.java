package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;

import com.example.contenthub.login.NaverSeriesLogin;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
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

    final static String BASE_URL = "https://series.naver.com";
    final static String TOP100 = "/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            for (int i = 1; i < 6; i++) {
                String url = BASE_URL + TOP100 + i;
                driver.get(url);
                wait.until(ExpectedConditions.urlToBe(url));

                naverLogin.activateBot(driver);

                // 로그인 후에 크롤링할 페이지로 이동
                driver.get(url);

                List<NovelData> list = getNaverSeriesData(driver);
                novels.addAll(list);
            }
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeWebDriver(driver);
        }
        return novels;
    }

    private List<NovelData> getNaverSeriesData(WebDriver driver) throws InterruptedException {
        List<NovelData> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"content\"]/div/ul/li"));
        for (WebElement element : novelElements) {
            WebElement novel = element.findElement(By.xpath(".//div[2]/h3/a"));
            String title = extractTitle(novel.getText());
            String detailHref = novel.getAttribute("href");
            String productId = extractProductId(detailHref);

            WebElement coverImgElement = element.findElement(By.xpath(".//a/img"));
            String coverImg = coverImgElement.getAttribute("src");
            String summary = element.findElement(By.xpath(".//div[2]/p[2]")).getText();

            String genre = getNovelGenre(detailHref);

            boolean adultContent = isAdultContent(element);

            NovelData novelData = new NovelData(title, coverImg, summary, genre, Arrays.asList(new SiteDTO(NovelSite.NAVER_SERIES.getName(), productId)), adultContent);

            novels.add(novelData);
        }
        return novels;
    }

    private String getNovelGenre(String href) {
        WebDriver driver = createWebDriver();
        try {
            String detailURL = href;
            driver.get(detailURL);

            By genreLocator = By.xpath("//*[@id=\"content\"]/ul[1]/li/ul/li[2]/span/a");
            waitForElement(driver, genreLocator);
            WebElement genreElement = driver.findElement(genreLocator);

            return genreElement.getText();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            closeWebDriver(driver);
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
