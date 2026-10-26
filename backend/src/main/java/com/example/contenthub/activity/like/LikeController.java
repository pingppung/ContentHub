package com.example.contenthub.activity.like;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contenthub.auth.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/activity")
public class LikeController {

    private final LikeService likeService;
    private final AuthService authService;

    @GetMapping("/like/status")
    public ResponseEntity<Boolean> getLikeStatus(@RequestParam String contentId) {

            String userId = authService.getAuthenticatedUser();

            boolean liked = likeService.getUserLikeStatus(userId, contentId);
            return ResponseEntity.ok(liked);
    }

    @PostMapping("/likes")
    public ResponseEntity<Void> addLike(@RequestBody LikeRequest likeRequest) {
       
            String userId = authService.getAuthenticatedUser();

            likeService.addLike(userId, likeRequest.getContentId());

            return ResponseEntity.status(HttpStatus.CREATED).build();
       
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Void> removeLike(
            @RequestBody LikeRequest likeRequest) {

        String userId = authService.getAuthenticatedUser();

        likeService.removeLike(userId, likeRequest.getContentId());

        return ResponseEntity.noContent().build();
    }
}
