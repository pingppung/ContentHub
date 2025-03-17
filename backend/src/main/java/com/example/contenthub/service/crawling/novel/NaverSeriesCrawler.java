package com.example.contenthub.service.crawling.novel;

import com.example.contenthub.constants.crawling.NaverNovelXPath;
import com.example.contenthub.dto.ContentCrawlDTO;
import com.example.contenthub.exception.CrawlerException;
import com.example.contenthub.service.auth.social.NaverSeriesLogin;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

@Component
@RequiredArgsConstructor
public class NaverSeriesCrawler {
    private final NaverSeriesLogin naverLogin;

    public List<ContentCrawlDTO> crawl(WebDriver mainDriver) {
        List<ContentCrawlDTO> novels = new ArrayList<>();
        try {
            setUpDrivers(mainDriver);
            int totalPages = getLastPageNumber(mainDriver);
            List<String> novelLinks = getNaverSeriesData(mainDriver, totalPages);

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
        mainDriver.get(NaverNovelXPath.URL.getXpath());
        naverLogin.activateBot(mainDriver);
    }

    public int getLastPageNumber(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement pagination = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath(NaverNovelXPath.PAGE_COUNT.getXpath())));
            List<WebElement> pageLinks = pagination.findElements(By.tagName("a"));
            int totalPages = pageLinks.size();
            return totalPages;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void navigateToPage(WebDriver driver, String url) {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(url));
    }

    private List<String> getNaverSeriesData(WebDriver webDriver, int totalPages) throws InterruptedException {
        List<String> novelLinks = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            String url = NaverNovelXPath.URL.getXpath() + i;
            navigateToPage(webDriver, url);

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            List<WebElement> elements = wait.until(driver -> driver.findElements(By.xpath(NaverNovelXPath.LIST.getXpath())));
            for (WebElement el : elements) {
                String href = el.findElement(By.xpath(".//a")).getAttribute("href");
                novelLinks.add(href);
            }
        }
        return novelLinks;
    }

    private ContentCrawlDTO extractNovelData(String detailHref, WebDriver webdriver) {
        String contentId = extractContentId(detailHref);
        String url = NaverNovelXPath.DETAIL_URL.getXpath() + contentId;
        // 상세 페이지 이동
        navigateToPage(webdriver, url);

        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(NaverNovelXPath.TITLE.getXpath())));

        // 상세 정보 추출
        String title = extractTitle(webdriver.findElement(By.xpath(NaverNovelXPath.TITLE.getXpath())).getText());
        String description = getDescription(webdriver);
        String coverImg = webdriver.findElement(By.xpath(NaverNovelXPath.COVER_IMG.getXpath())).getAttribute("src");
        boolean isAdultContent = isAdultContent(webdriver);
        String genre = webdriver.findElement(By.xpath(NaverNovelXPath.GENRE.getXpath())).getText();
        return new ContentCrawlDTO(title, description, coverImg, genre, isAdultContent, contentId);
    }

    private boolean isAdultContent(WebDriver driver) {
        return driver.findElements(By.xpath(NaverNovelXPath.IS_ADULT.getXpath())).size() > 0;
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

    private String getDescription(WebDriver driver) {
        try {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(By.xpath(NaverNovelXPath.DESCRIPTION_WITH_MORE.getXpath()));
            String des = (String) js.executeScript("return arguments[0].innerText.trim();", element);
            return cleanText(des);
        } catch (NoSuchElementException e) {
            try {
                return driver.findElement(By.xpath(NaverNovelXPath.DESCRIPTION_NO_MORE.getXpath())).getText();
            } catch (NoSuchElementException ex) {
                return "설명 없음";
            }
        }
    }
    private String cleanText(String text) {
        return text.replaceAll("[\\u00A0\\u200B\\u200C\\u200D\\uFEFF]", " ")
                    .replace("접기", "")
                    .trim();
    }

}
