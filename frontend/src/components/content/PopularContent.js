import React, { useState, useEffect } from 'react';
import styles from '../css/PopularContent.module.css';

function PopularContent({ contents }) {
    const [currentIndex, setCurrentIndex] = useState(0);

    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentIndex((prevIndex) =>
                prevIndex === contents.length - 1 ? 0 : prevIndex + 1
            );
        }, 5000);

        return () => clearInterval(timer);
    }, [contents.length]);

    useEffect(() => {
        // 현재 선택된 이미지를 배경으로 설정
        const imageWrappers = document.querySelectorAll(`.${styles.imageWrapper}`);
        imageWrappers.forEach(wrapper => {
            const img = wrapper.querySelector('img');
            if (img) {
                wrapper.style.setProperty('--bg-image', `url(${img.src})`);
            }
        });
    }, [currentIndex, contents]);

    const handlePrevClick = () => {
        setCurrentIndex((prevIndex) =>
            prevIndex === 0 ? contents.length - 1 : prevIndex - 1
        );
    };

    const handleNextClick = () => {
        setCurrentIndex((prevIndex) =>
            prevIndex === contents.length - 1 ? 0 : prevIndex + 1
        );
    };

    const getVisibleContents = () => {
        const items = [];
        for (let i = -1; i <= 1; i++) {
            const index = (currentIndex + i + contents.length) % contents.length;
            items.push(contents[index]);
        }
        return items;
    };

    if (!contents || contents.length === 0) {
        return null;
    }
    console.log(contents);
    return (
        <div className={styles.popularContainer}>
            <div className={styles.carouselWrapper}>
                {getVisibleContents().map((content, index) => (
                    <div
                        key={index}
                        className={`${styles.carouselItem} ${index === 1 ? styles.active : ''}`}
                    >
                        <div className={styles.imageWrapper}>
                            <img
                                src={content.coverImg}
                                alt={content.title}
                                className={styles.contentImage}
                            />
                            <div className={styles.contentInfo}>
                                <div className={styles.genreTag}>{content.genre}</div>
                                <h2>{content.title}</h2>
                                <p>{content.summary}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <div className={styles.navigationButtons}>
                <button
                    className={styles.navButton}
                    onClick={handlePrevClick}
                >
                    ‹
                </button>
                <button
                    className={styles.navButton}
                    onClick={handleNextClick}
                >
                    ›
                </button>
            </div>
        </div>
    );
}

export default PopularContent; 