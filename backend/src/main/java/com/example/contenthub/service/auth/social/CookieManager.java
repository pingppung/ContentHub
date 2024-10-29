package com.example.contenthub.service.auth.social;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

@Slf4j
public class CookieManager {

    public void saveCookies(WebDriver driver, String filePath) {
        File file = new File(filePath);
        try {
            // file.delete(); //옛날 파일 삭제
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Cookie cookie : driver.manage().getCookies()) {
                bw.write((cookie.getName() + ";" + cookie.getValue() + ";"
                        + cookie.getDomain() + ";" + cookie.getPath() + ";"
                        + cookie.getExpiry() + ";" + cookie.isSecure()));

                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCookies(WebDriver driver, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(line, ";");
                while (token.hasMoreTokens()) {
                    String name = token.nextToken();
                    String value = token.nextToken();
                    String domain = token.nextToken();
                    String path = token.nextToken();
                    Date expiry = null;

                    String expiryStr;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    if (!(expiryStr = token.nextToken()).equals("null")) {
                        try {
                            expiry = dateFormat.parse(expiryStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Boolean isSecure = Boolean.parseBoolean(token.nextToken());

                    Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
                    driver.manage().addCookie(cookie);

                }

            }
        } catch (IOException e) {
            log.error("IOException occurred: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
