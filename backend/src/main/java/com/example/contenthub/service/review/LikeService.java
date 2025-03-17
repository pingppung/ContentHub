package com.example.contenthub.service.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.contenthub.entity.Like;
import com.example.contenthub.entity.User;
import com.example.contenthub.repository.LikeRepository;
import com.example.contenthub.repository.ContentRepository;
import com.example.contenthub.repository.UserRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ContentRepository novelRepository;

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
}