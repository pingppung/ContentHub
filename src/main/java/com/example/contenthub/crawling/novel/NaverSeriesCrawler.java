package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;

import com.example.contenthub.login.CookieManager;
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
import java.util.Set;
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


    final static String BASE_URL = "https://series.naver.com";
    final static String TOP100 = "/novel/top100List.series?";
    final static String TYPECODEPARAM = "rankingTypeCode=HOURLY";
    final static String CATEGORYCODEPARAM = "categoryCode=ALL";
    final static String PAGEPARAM = "page=";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();
        WebDriver driver = createWebDriver();
        WebDriver detailDriver = createWebDriver();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String url = BASE_URL + TOP100 + TYPECODEPARAM + "&" + CATEGORYCODEPARAM;

            driver.get(BASE_URL);
            detailDriver.get(url);
            WebElement closeButtonLeft = driver.findElement(By.className("close_btn_left"));
            if (closeButtonLeft.isDisplayed()) {
                // 버튼이 보이면 클릭
                closeButtonLeft.click();
            }
            naverLogin.activateBot(driver); //일단 커버이미지 때문에 로그인 상태로 만들어놓음
            naverLogin.activateBot(detailDriver);
//            CookieManager.saveCookies(driver, "NaverSeries.data");

            for (int i = 1; i < 6; i++) {
                driver.get(url + "&" + PAGEPARAM + i);
                wait.until(ExpectedConditions.urlToBe(url + "&" + PAGEPARAM + i));

                List<NovelData> list = getNaverSeriesData(driver, detailDriver);
                novels.addAll(list);
            }
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeWebDriver(driver);
            closeWebDriver(detailDriver);
        }
        return novels;
    }

    private List<NovelData> getNaverSeriesData(WebDriver driver, WebDriver detailDriver) throws InterruptedException {


        List<NovelData> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"content\"]/div/ul/li"));
        for (WebElement element : novelElements) {
            WebElement novel = element.findElement(By.xpath(".//div[2]/h3/a"));
            String title = extractTitle(novel.getText());
            if (novelCrawlerService.isDataExist(title, NovelSite.NAVER_SERIES.getName())) continue;

            String detailHref = novel.getAttribute("href");
            String productId = extractProductId(detailHref);


            boolean adultContent = isAdultContent(element);

            WebElement coverImgElement = element.findElement(By.xpath(".//a/img"));
            String coverImg = coverImgElement.getAttribute("src");
            String summary = element.findElement(By.xpath(".//div[2]/p[2]")).getText();

            String genre = getNovelGenre(detailDriver, detailHref, adultContent);

            NovelData novelData = new NovelData(title, coverImg, summary, genre, Arrays.asList(new SiteDTO(NovelSite.NAVER_SERIES.getName(), productId)), adultContent);

            novels.add(novelData);
        }
        return novels;
    }

    private String getNovelGenre(WebDriver driver, String href, boolean isAdult) {
        try {
            String detailURL = href;

            driver.get(detailURL);
//            if (isAdult) {
//                //CookieManager.loadCookies(driver, "NaverSeries.data");
//                driver.get(detailURL);
//            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"content\"]/ul[1]/li/ul/li[2]/span/a")));

            By genreLocator = By.xpath("//*[@id=\"content\"]/ul[1]/li/ul/li[2]/span/a");
            //waitForElement(driver, genreLocator);
            WebElement genreElement = driver.findElement(genreLocator);

            return genreElement.getText();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
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
