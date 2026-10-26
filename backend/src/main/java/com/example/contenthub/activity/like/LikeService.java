package com.example.contenthub.activity.like;

import org.springframework.stereotype.Service;

import com.example.contenthub.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;

    public void addLike(String userId, String contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new IllegalArgumentException("존재하지 않는 콘텐츠입니다.");
        }

        if (likeRepository.existsByUserIdAndContentId(userId, contentId)) {
            return;
        }

        Like like = new Like(userId, contentId);

        likeRepository.save(like);

    }

    public void removeLike(String userId, String contentId) {

        likeRepository.findByUserIdAndContentId(userId, contentId)
                .ifPresent(likeRepository::delete);
    }

    public boolean getUserLikeStatus(String userId, String contentId) {
       return likeRepository.existsByUserIdAndContentId(
                userId,
                contentId
        ); 
    }

    public long getLikeCount(String contentId) {

        return likeRepository.countByContentId(contentId);
    }
}