package com.example.contenthub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contenthub.domain.User;
import com.example.contenthub.service.auth.AuthService;
import com.example.contenthub.service.review.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/activity")
public class UserActivityController {

    private final LikeService likeService;
    private final AuthService authService;

    @GetMapping("/like/status")
    public ResponseEntity<Boolean> getLikeStatus(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "category") String category) {

        try {
            User authenticatedUser = authService.getAuthenticatedUser();
          //  boolean liked = likeService.checkLikeStatus(authenticatedUser, title, category);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

}
