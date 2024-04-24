package com.example.contenthub;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ContenthubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContenthubApplication.class, args);
    }
}
