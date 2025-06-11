package com.example.contenthub.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.contenthub.repository.LikeRepository;
import com.example.contenthub.domain.Content;
import com.example.contenthub.domain.Like;
import com.example.contenthub.domain.User;
import com.example.contenthub.repository.ContentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;

    public void addLike(User user, String title, String category) {
        String userId = user.getUserId();
        Like like = likeRepository.findByUserId(userId);
        Content content = contentRepository.findByCategoryAndTitle(category, title);
        if (like == null) {
            // 없으면 새로 만들고 content 추가
            System.out.println("추가");
            like = new Like(userId, content.getId());
        } else {
            // 있으면 content 추가 (중복 방지 포함)
            like.addContent(content.getId());
        }
        likeRepository.save(like);
    }

    public void removeLike(User user, String title, String category) {
        Content content = contentRepository.findByCategoryAndTitle(category, title);

        // 해당 유저와 콘텐츠로 저장된 like 찾아오기
        Like like = likeRepository.findByUserIdAndContentIds(user.getUserId(), content.getId());

        if (like != null) {
            likeRepository.delete(like);
        }
    }

    public boolean getUserLikeStatus(User user, String title, String category) {
        try {
            System.out.println(user + " " + title + " " + category);
            Content content = contentRepository.findByCategoryAndTitle(category, title);

            if (content == null) {
                System.out.println("Content not found!");
                return false;
            }
            boolean likeExists = likeRepository.existsByUserIdAndContentIds(user.getUserId(), content.getId());
            System.out.println("Like exists: " + likeExists);
            return likeExists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}