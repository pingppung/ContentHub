package com.example.contenthub.service.crawling.novel;

import com.example.contenthub.constants.crawling.KakaoNovelXPath;
import com.example.contenthub.constants.crawling.NaverNovelXPath;
import com.example.contenthub.dto.ContentCrawlDTO;
import com.example.contenthub.entity.Content;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class KakaoPageCrawler {
    private final KakaoPageLogin kakaoLogin;

    public List<ContentCrawlDTO> crawl(WebDriver mainDriver) {
        List<ContentCrawlDTO> novels = new ArrayList<>();
        try {
            setUpDrivers(mainDriver);

            scrollPageToBottom(mainDriver);

            List<String> novelLinks = getKakaoPageData(mainDriver);
            for (String href : novelLinks) {
                try {
                    ContentCrawlDTO content = extractNovelData(href, mainDriver);
                    novels.add(content);
                } catch (TimeoutException e) {
                    System.err.println("TimeoutException 발생 - 다음 항목으로 진행: " + href);
                } catch (Exception e) {
                    System.err.println("예상치 못한 오류 발생 - 다음 항목으로 진행: " + href);
                    //e.printStackTrace();
                }
            }
        } catch (TimeoutException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.TIMEOUT_EXCEPTION.getMessage(), e);
        } catch (InterruptedException e) {
            throw new CrawlerException(CrawlerException.ExceptionMessage.UNEXPECTED_EXCEPTION.getMessage(), e);
        }
        return novels;
    }

    private void setUpDrivers(WebDriver mainDriver) {
        mainDriver.get(KakaoNovelXPath.URL.getXpath());
        kakaoLogin.activateBot(mainDriver);
    }

    private void scrollPageToBottom(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (true) {
            Long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
            if (newHeight.equals(lastHeight)) {
                break;
            }
        }
    }

    private List<String> getKakaoPageData(WebDriver webDriver) {
        List<String> novelLinks = new ArrayList<>();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        List<WebElement> elements = wait.until(driver -> driver.findElements(By.xpath(KakaoNovelXPath.LIST.getXpath())));

        for (WebElement el : elements) {
            String href = el.findElement(By.xpath(".//a")).getAttribute("href");
            novelLinks.add(href);
        }
        return novelLinks;
    }

    private void navigateToPage(WebDriver driver, String url) {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(url));
    }

    private ContentCrawlDTO extractNovelData(String detailHref, WebDriver webDriver) {
        String contentId = extractContentId(detailHref);
        String url = KakaoNovelXPath.DETAIL_URL.buildUrl(contentId);
        // 상세 페이지 이동
        navigateToPage(webDriver, url);

        // 상세 정보 추출
        String originalTitle = webDriver.findElement(By.xpath(KakaoNovelXPath.TITLE.getXpath())).getText();
        boolean isAdultContent = containsAdultTag(originalTitle); // 성인 여부 체크
        String title = extractTitle(originalTitle); // 실제 제목 정리
        String description = webDriver.findElement(By.xpath(KakaoNovelXPath.DESCRIPTION.getXpath())).getText();
        String coverImg = webDriver.findElement(By.xpath(KakaoNovelXPath.COVER_IMG.getXpath())).getAttribute("src");
        String genre = webDriver.findElement(By.xpath(KakaoNovelXPath.GENRE.getXpath())).getText();
        return new ContentCrawlDTO(title, description, coverImg, genre, isAdultContent, contentId);
    }

    private static boolean containsAdultTag(String title) {
        // [] 안의 내용을 추출해서 "19세"가 포함되어 있는지 확인
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(title);

        while (matcher.find()) {
            if (matcher.group(1).contains("19세")) {
                return true; // 성인 콘텐츠로 판단
            }
        }
        return false;
    }

    private static String extractTitle(String title) {
        return title.replaceAll("\\[.*?\\]", "").trim();
    }

    private String extractContentId(String input) {
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
