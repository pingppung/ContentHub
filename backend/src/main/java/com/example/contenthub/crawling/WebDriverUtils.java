package com.example.contenthub.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverUtils {
    public static WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver"); // Docker 컨테이너 내 경로
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        //chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        return driver;
    }

    public static void closeWebDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void waitForElement(WebDriver driver, By by) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
