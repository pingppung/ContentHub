import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import axios from "axios";
import styles from "../css/ContentOptions.module.css";
import EmojiRating from "../review/EmojiRating";
import Review from "../review/ReviewSection";
import Content from "./Content";

const ContentOptions = ({ content, category }) => {
    const [activeTab, setActiveTab] = useState("review");
    const [similarContent, setSimilarContent] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    useEffect(() => {
        const fetchSimilarContent = async () => {
            if (!content) return;
            try {
                const response = await axios.post(
                    "/recommend/similar",
                    {
                        query: content,
                        category: category,
                    },
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );
                console.log(response);
                setSimilarContent(response.data.top_contents || []);
            } catch (error) {
                console.error("Error fetching similar content:", error);
            }
        };

        if (activeTab === "similar") {
            fetchSimilarContent();
        }
    }, [activeTab, content, category]);

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };

    const handleContentClick = (clickedContent) => {
        console.log("Content clicked:", clickedContent);
        const query = new URLSearchParams({
            title: clickedContent.title,
        }).toString();
        navigate(`/content/${category}/detail/${query}`, {
            state: { background: location, clickedContent, category },
        });
    };

    return (
        <div className={styles.contentOptions}>
            <div className={styles.tabButtons}>
                <button
                    className={activeTab === "review" ? styles.activeTab : ""}
                    onClick={() => handleTabClick("review")}
                >
                    리뷰
                </button>
                <button
                    className={activeTab === "similar" ? styles.activeTab : ""}
                    onClick={() => handleTabClick("similar")}
                >
                    비슷한 작품
                </button>
            </div>
            <div className={styles.tabContent}>
                {activeTab === "review" && (
                    <div>
                        <section className={styles.emojiSection}>
                            <EmojiRating />
                        </section>
                        <section className={styles.reviewSection}>
                            <Review />
                        </section>
                    </div>
                )}
                {activeTab === "similar" && (
                    <div className={styles.similarContentContainer}>
                        {similarContent.length > 0 ? (
                            similarContent.map((item, index) => (
                                <Content
                                    key={index}
                                    title={item.title}
                                    coverImg={item.coverImg}
                                    synopsis={item.synopsis}
                                    genre={item.genre || []}
                                    openNovelDetail={() => handleContentClick(item)}
                                />
                            ))
                        ) : (
                            <p>비슷한 작품을 찾을 수 없습니다.</p>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ContentOptions;
