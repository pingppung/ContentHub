package com.example.contenthub.login;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class NaverSeriesLogin {
    @Value("${naver.service.id}")
    private String naverId;

    @Value("${naver.service.pwd}")
    private String naverPwd;


    // https://nid.naver.com/nidlogin.login 네이버 로그인 url

    public void activateBot(WebDriver driver) {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.className("gnb_btn_login")));
            loginLink.click();
            // 아이디 입력
            WebElement id = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("id")));
            //id.sendKeys(naverId);
            js.executeScript("arguments[0].value=arguments[1]", id, naverId);
            Thread.sleep(1500);

            // 비밀번호 입력
            WebElement pwd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pw")));
            //pwd.sendKeys(naverPwd);
            js.executeScript("arguments[0].value=arguments[1]", pwd, naverPwd);
            Thread.sleep(1500);

            // 전송
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn_login")));

            loginButton.click();

            wait.until(ExpectedConditions.urlContains("/novel"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
