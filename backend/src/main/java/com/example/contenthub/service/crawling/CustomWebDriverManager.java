package com.example.contenthub.service.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@RequiredArgsConstructor
public class CustomWebDriverManager {
    private static WebDriver mainDriver;

    public static WebDriver getDriver() {
        if (mainDriver == null) {
            try {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                mainDriver = new ChromeDriver(options);
               // detailDriver = new ChromeDriver(options);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create WebDriver instance", e);
            }
        }
        return mainDriver;
    }

    public static void closeDriver() {
        if (mainDriver != null) {
            mainDriver.quit();
            //detailDriver.quit();
            mainDriver = null;
           // detailDriver = null;
        }
    }
}
