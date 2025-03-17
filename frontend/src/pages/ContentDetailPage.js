import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import styles from "./css/ContentDetail.module.css";
import { AiFillHeart, AiOutlineHeart } from "react-icons/ai";
import UserActivityService from '../services/UserActivityService';
import Header from "../components/Header";

function ContentDetail() {
  const [liked, setLiked] = useState(false);
  const [count, setCount] = useState();
  const { search, state } = useLocation();
  const [category, SetCategory] = useState("novel");

  const location = useLocation();
  const navigate = useNavigate();
  const { background, content } = location.state || {};
  console.log(content);
  // useEffect(() => {

  //   const fetchLikeStatus = async () => {
  //     try {
  //       const { liked } = await UserActivityService.checkLikeStatus(contentId, category);
  //       setLiked(liked);
  //     } catch (error) {
  //       console.error('Failed to fetch like status:', error);
  //     }
  //   };
  //   fetchLikeStatus();
  // }, [contentId, category]);
  const platformIcons = {
    '네이버시리즈': 'https://ssl.pstatic.net/static/nstore/series_favicon_152.ico', // 네이버 시리즈 아이콘
    '카카오페이지': 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Kakao_page_logo.png/250px-Kakao_page_logo.png',  // 카카오 페이지 아이콘
    '네이버웹툰': 'https://your-icon-url.com/naver-webtoon-icon.png', // 네이버 웹툰 아이콘
    '왓챠': 'https://play-lh.googleusercontent.com/vAkKvTtE8kdb0MWWxOVaqYVf0_suB-WMnfCR1MslBsGjhI49dAfF1IxcnhtpL3PnjVY=w240-h480-rw', // 왓챠 아이콘
    '넷플릭스': 'https://your-icon-url.com/netflix-icon.png', // 넷플릭스 아이콘
    '디즈니플러스': 'https://your-icon-url.com/disney-plus-icon.png', // 디즈니플러스 아이콘
    '티빙': 'https://your-icon-url.com/tving-icon.png', // 티빙 아이콘
    '웨이브': 'https://your-icon-url.com/wavve-icon.png', // 웨이브 아이콘
    '기타': 'https://your-icon-url.com/default-icon.png', // 기본 아이콘
  };

  const getIconForSite = (platform) => {
    const iconUrl = platformIcons[platform] || platformIcons['기타'];
    return <img src={iconUrl} alt={`${platform} 아이콘`} style={{ width: '50px', height: '50px' }} />;
  };

  const handleLikeToggle = async () => {
    // try {
    //   if (liked) {
    //     await removeLike(contentId, category);
    //   } else {
    //     await addLike(contentId, category);
    //   }
    //   setLiked(!liked); // 상태 변경

    //   setCount(prevCount => (liked ? prevCount - 1 : prevCount + 1));
    // } catch (error) {
    //   console.error('Failed to toggle like:', error);
    // }
  };

  // const getSiteUrl = (siteName) => {
  //   if (siteName === "NaverSeries") {
  //     return "https://series.naver.com/novel/detail.series?productNo=";
  //   } else {
  //     return 'Unknown site';
  //   }
  // }
  if (!content) {
    return <p>Loading...</p>;
  }
  const handleClose = () => {
    navigate(-1); // 뒤로 가기 (모달 닫기)
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContentWrapper}>
        <section className={styles.modalBody}>
          <header className={styles.modalHeader}>
            <h2 className={styles.contentTitle}>{content.title}</h2>
            <button className={styles.closeButton} onClick={handleClose}>X</button>
          </header>

          <div className={styles.modalContent}>
            <img src={content.coverImg} alt={content.title} className={styles.coverImg} />
            <div className={styles.contentDetails}>
              <h4 className={styles.genre}>{content.genre}</h4>
              <p>{content.description}</p>
            </div>
          </div>
        </section>

        {content.links.map((item, index) => (
          <p key={index}>
            <a href={item.url} target="_blank" rel="noopener noreferrer">
              {getIconForSite(item.platform)}
            </a>
          </p>
        ))}
        <div className={styles.likeSection}>
          <button
            className={`${styles.likeButton} ${liked ? styles.liked : ""}`}
            onClick={handleLikeToggle}
          >
            <span className={styles.icon}>
              {liked ? <AiFillHeart size={24} /> : <AiOutlineHeart size={24} />}
            </span>
            <span className={styles.count}>{count}</span>
          </button>
        </div>
      </div>
    </div>
  );

}

export default ContentDetail;