package com.example.contenthub;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HubController {
    @GetMapping("/api/data")
    public String test() {
        return "Hello, pingppung!";
    }
}