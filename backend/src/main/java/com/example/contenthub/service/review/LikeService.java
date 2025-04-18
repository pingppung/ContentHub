package com.example.contenthub.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.contenthub.repository.LikeRepository;
import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.Like;
import com.example.contenthub.domain.User;
import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;

    // public void addLike(User user, Long contentId, Like.Category category) {
    //     boolean isValid = false;

    //     switch (category) {
    //         case NOVEL:
    //             isValid = novelRepository.existsById(contentId);
    //             break;
    //         // case WEBTOON:
    //         //     isValid = webtoonRepository.existsById(contentId);
    //         //     break;
    //         // case DRAMA:
    //         //     isValid = dramaRepository.existsById(contentId);
    //         //     break;
    //     }

    //     if (!isValid) {
    //         throw new IllegalArgumentException("contentId 또는 category가 이상합니다다");
    //     }

    //     Like like = new Like(user, contentId, category);
    //     likeRepository.save(like);
    // }
    // public boolean checkLikeStatus(User user, String title, String category) {
    //     try {
    //         System.out.println(user + " " + title + " " + category);
    //         Content content = contentRepository.findByTitleAndCategory(title, category);

    //         if (content == null) {
    //             System.out.println("Content not found!");
    //             return false;
    //         }

    //         System.out.println(content);
    //         boolean exists = likeRepository.existsByUserAndContent(user, content);
    //         System.out.println("Like exists: " + exists);
    //         return exists;
    //     } catch (Exception e) {
    //         System.err.println("Error in fefe: " + e.getMessage());
    //         e.printStackTrace(); // 예외 스택 트레이스를 출력하여 문제를 추적
    //         return false;
    //     }
    // }

}