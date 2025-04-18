import React, { useState } from 'react';
import styles from './css/ContentOptions.module.css';
import EmojiRating from './EmojiRating';
import Review from './Review';

const ContentOptions = () => {
    const [activeTab, setActiveTab] = useState('review');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };

    return (
        <div className={styles.contentOptions}>
            <div className={styles.tabButtons}>
                <button
                    className={activeTab === 'review' ? styles.activeTab : ''}
                    onClick={() => handleTabClick('review')}
                >
                    리뷰
                </button>
                <button
                    className={activeTab === 'similar' ? styles.activeTab : ''}
                    onClick={() => handleTabClick('similar')}
                >
                    비슷한 작품
                </button>
            </div>
            <div className={styles.tabContent}>
                {activeTab === 'review' && (
                    <div>
                        <section className={styles.emojiSection}>
                            <EmojiRating />
                        </section>
                        <section className={styles.reviewSection}>
                            <Review />
                        </section>
                    </div>
                )}
                {activeTab === 'similar' && (
                    <div>
                        <h3>비슷한 작품 섹션</h3>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ContentOptions;
