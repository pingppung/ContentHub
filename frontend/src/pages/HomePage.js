import React, { useEffect, useState } from "react";
import Header from "../components/common/Header";
import styles from "./css/Home.module.css";

function Home() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(false);
  }, []);

  return (
    <div className={styles.container}>
      <Header />
      {loading ? (
        <div className={styles.loader}>
          <span>Loading...</span>
        </div>
      ) : (
        <main>
          {/* 메인 배너 섹션 */}
          <section className={styles.banner}>
            <div className={styles.bannerContent}>
              <h1>콘텐츠 허브</h1>
              <p>다양한 콘텐츠 정보를 한눈에</p>
            </div>
          </section>

          {/* 검색 섹션 */}
          <section className={styles.search}>
            <div className={styles.searchContainer}>
              <input type="text" placeholder="검색어를 입력하세요" />
              <button>검색</button>
            </div>
          </section>

          {/* 카테고리 섹션 */}
          <section className={styles.categories}>
            <h2>카테고리</h2>
            <div className={styles.categoryList}>
              {/* 카테고리 목록이 들어갈 자리 */}
            </div>
          </section>

          {/* 최신 콘텐츠 섹션 */}
          <section className={styles.latest}>
            <h2>최신 콘텐츠</h2>
            <div className={styles.contentList}>
              {/* 최신 콘텐츠 목록이 들어갈 자리 */}
            </div>
          </section>

          {/* 인기 콘텐츠 섹션 */}
          <section className={styles.popular}>
            <h2>인기 콘텐츠</h2>
            <div className={styles.contentList}>
              {/* 인기 콘텐츠 목록이 들어갈 자리 */}
            </div>
          </section>

          {/* 추천 콘텐츠 섹션 */}
          <section className={styles.recommended}>
            <h2>추천 콘텐츠</h2>
            <div className={styles.contentList}>
              {/* 추천 콘텐츠 목록이 들어갈 자리 */}
            </div>
          </section>
        </main>
      )}
    </div>
  );
}

export default Home;
