package com.example.contenthub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contenthub.domain.User;
import com.example.contenthub.dto.LikeRequest;
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
            // 인증된 사용자 정보 가져오기
            User authenticatedUser = authService.getAuthenticatedUser();
            boolean liked = likeService.getUserLikeStatus(authenticatedUser, title, category);
            return ResponseEntity.ok(liked);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @PostMapping("/likes")
    public ResponseEntity<Void> addLike(@RequestBody LikeRequest likeRequest) {
        try {
            User authenticatedUser = authService.getAuthenticatedUser();
            likeService.addLike(authenticatedUser, likeRequest.getTitle(), likeRequest.getCategory());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("likes")
    public ResponseEntity<Void> removeLike(@RequestBody LikeRequest likeRequest) {
        try {
            User authenticatedUser = authService.getAuthenticatedUser();
            likeService.removeLike(authenticatedUser, likeRequest.getTitle(), likeRequest.getCategory());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
