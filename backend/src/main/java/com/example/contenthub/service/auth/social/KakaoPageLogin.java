package com.example.contenthub.service.auth.social;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class KakaoPageLogin {
    @Value("${kakao.service.id}")
    private String kakaoId;

    @Value("${kakao.service.pwd}")
    private String kakaoPwd;

    public void activateBot(WebDriver driver) {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.className("pr-16pxr")));
            loginLink.click();
            // 아이디 입력
            WebElement id = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loginId--1")));
            id.sendKeys(kakaoId);
            //js.executeScript("arguments[0].value=arguments[1]", id, kakaoId);
            Thread.sleep(1500);

            // 비밀번호 입력
            WebElement pwd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password--2")));
            pwd.sendKeys(kakaoPwd);
            //js.executeScript("arguments[0].value=arguments[1]", pwd, kakaoPwd);
            Thread.sleep(1500);

            // 전송
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn_g.highlight.submit")));
            loginButton.click();
            Thread.sleep(1500);

            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn_agree")));
            Thread.sleep(1500);
            continueButton.click();
            wait.until(ExpectedConditions.urlContains("/menu"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
