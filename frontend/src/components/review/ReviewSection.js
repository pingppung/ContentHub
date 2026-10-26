import React, { useState } from 'react';
import ReviewList from './ReviewList';
import styles from '../css/Review.module.css';

const ReviewSection = () => {
    const [comment, setComment] = useState("");
    const [comments, setComments] = useState([]);

    const handleAddComment = () => {
        if (!comment.trim()) return;
        setComments([...comments, comment]);
        setComment("");
    };

    const dummyComments = [
        {
            id: 1,
            username: 'user1',
            content: '진짜 재밌었어요!',
            createdAt: '2024-04-01',
        },
        {
            id: 2,
            username: 'user2',
            content: '생각보다 평범했어요.',
            createdAt: '2024-04-02',
        },
    ];

    return (
        <div>
            <div className={styles.reviewInput}>
                <textarea
                    className={styles.textArea}
                    rows="3"
                    placeholder="댓글을 입력하세요..."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    onKeyDown={(e) => e.key === "Enter" && !e.shiftKey && handleAddComment()}
                />
                <button className={styles.commentBtn} onClick={handleAddComment}>등록</button>
            </div>
            <div className={styles.reviewHeader}>
                리뷰({dummyComments.length})
            </div>
            <div className={styles.reviews}>
                <ReviewList comments={dummyComments} />
            </div>
        </div>
    );
};

export default ReviewSection; 