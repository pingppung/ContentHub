package com.example.contenthub.crawling.novel;

import com.example.contenthub.crawling.SiteDTO;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class KakaoPageCrawler {
    final static String base_url = "https://page.kakao.com";
    final static String top300 = "/menu/10011/screen/94";

    public List<NovelData> crawl() {
        List<NovelData> novels = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        WebDriver driver = new ChromeDriver(chromeOptions);


        try {
            String url = base_url + top300;
            driver.get(url);

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

            // 현재 페이지의 소스를 가져오기
            String pageSource = driver.getPageSource();
            getKakaoPageData(driver, pageSource);
            //List<NovelData> list = getNaverSeriesData(driver, pageSource);
            //novels.addAll(list);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }

        return novels;
    }

    private void getKakaoPageData(WebDriver driver, String pageSource) {
        List<NovelData> novels = new ArrayList<>();
        List<WebElement> novelElements = driver.findElements(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div"));
        //List<WebElement> novelElements = parentDiv.findElements(By.tagName("div"));
        System.out.println(novelElements.size());
        //System.out.println(pageSource);

    }

    //작품번호 추출
    private String extractProductId(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

}
