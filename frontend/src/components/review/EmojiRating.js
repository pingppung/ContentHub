import React, { useState } from 'react';
import Rating from '@mui/material/Rating';
import styles from '../css/EmojiRating.module.css';

const EmojiRating = () => {
    const [rating, setRating] = useState(null);
    const [hoverRating, setHoverRating] = useState(-1);
    const customIcons = {
    1: { icon: '😡', label: '으이구, 다시는 보고 싶지 않다!' },
    2: { icon: '😕', label: '아쉽지만... 내 취향은 아니었어' },
    3: { icon: '🙂', label: '나쁘진 않은데, 특별하지도 않음' },
    4: { icon: '😆', label: '오~ 이거 꽤 재밌는데?' },
    5: { icon: '🤩', label: '레전드... 당장 봐라!' },
};

    const IconContainer = ({ value, ...props }) => {
        return <span {...props}>{customIcons[value]?.icon || null}</span>;
    };

    return (
        <div className={styles.rating}>
            <div className={styles.label}>{customIcons[hoverRating !== -1 ? hoverRating : rating]?.label || "당신의 솔직한 평가를 알려주세요!"}</div>
            <Rating
                className={styles.icon}
                value={rating}
                onChange={(_, newRating) => setRating(newRating)}
                onChangeActive={(event, newHover) => {
                    setHoverRating(newHover);
                }}
                getLabelText={(value) => customIcons[value]?.label}
                highlightSelectedOnly
                IconContainerComponent={IconContainer}
                sx={{
                    '& .MuiRating-iconEmpty .MuiSvgIcon-root': {
                        color: 'lightgray',
                    },
                }}
            />
        </div>
    );
};

export default EmojiRating;
