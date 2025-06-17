import React, { useEffect, useState } from "react";
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import axios from "axios";
import Content from "../components/content/Content";
import GenreButtons from "../components/common/GenreButtons";
import Search from "../components/common/Search";
import PopularContent from "../components/content/PopularContent";
import { getContentData, getContentBySearch } from "../services/ContentAPIService";
import styles from "./css/ContentHome.module.css";
import Header from "../components/common/Header";

function ContentHome() {

  const { category, genre, title } = useParams();
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);
  const [popularContent, setPopularContent] = useState([]);
  const [latestContent, setLatestContent] = useState([]);
  const [recommendedContent, setRecommendedContent] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState("전체");
  const [searchInput, setSearchInput] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 105;
  const navigate = useNavigate();
  const params = useParams();
  const location = useLocation();
  useEffect(() => {
    const category = params.category;
    //const apiUrl = selectedGenre !== "전체" ? `/novel?genre=${selectedGenre}` : "/novel";

    const encodedTitle = encodeURIComponent(searchInput);
    getContentBySearch(category, selectedGenre, searchInput).then((res) => {
      setDate(res);
      console.log(res);
      setLoading(false);
    });

    // 인기 콘텐츠 데이터 가져오기 (임시로 같은 데이터 사용)
    getContentBySearch(category, "전체", "").then((res) => {
      setPopularContent(res.slice(0, 5)); // 상위 5개만 사용
    });
  }, [category, selectedGenre, searchInput]);

  const getCurrentPageData = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return data.slice(startIndex, endIndex);
  };

  const totalPages = Math.ceil(data.length / itemsPerPage);

  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setCurrentPage(newPage);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  const openContentDetail = (content) => {
    console.log(content)
    const query = new URLSearchParams({
      title: content.title,
    }).toString();
    navigate(`/content/${category}/detail/${query}`, { state: { background: location, content, category } });

  };

  const handleGenreClick = (genre) => {
    setSelectedGenre(genre);
  };

  const handleTitleInput = (inputTitle) => {
    setSearchInput(inputTitle);
  };

  const getPageNumbers = () => {
    const pageNumbers = [];
    const maxPages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxPages / 2));
    let endPage = Math.min(totalPages, startPage + maxPages - 1);

    if (endPage - startPage + 1 < maxPages) {
      startPage = Math.max(1, endPage - maxPages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }
    return pageNumbers;
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className={styles.container}>
      <Header />
      {loading ? (
        <div className={styles.loader}>
          <span>Loading...</span>
        </div>
      ) : (
        <>
          {/* <div className={styles.searchSection}>
            <Search handleTitleInput={handleTitleInput} />
          </div> */}

          {/* 인기 콘텐츠 섹션 */}
          <section className={styles.popularSection}>
            <h2>인기 콘텐츠</h2>
            <PopularContent contents={popularContent} />
          </section>

          {/* 장르 섹션 */}
          <div className={styles.genreSection}>
            <GenreButtons
              category={category}
              selectedGenre={selectedGenre}
              handleGenreClick={handleGenreClick}
            />
          </div>

          {/* 메인 콘텐츠 그리드 */}
          <section className={styles.mainContentSection}>
            <div className={styles.contentHeader}>
              <h2>전체 콘텐츠</h2>
              <div className={styles.contentFilters}>
                <div className={styles.filterGroup}>
                  <span>정렬:</span>
                  <select className={styles.filterSelect}>
                    <option value="latest">최신순</option>
                    <option value="popular">인기순</option>
                    <option value="rating">평점순</option>
                  </select>
                </div>
                <div className={styles.filterGroup}>
                  <span>상태:</span>
                  <select className={styles.filterSelect}>
                    <option value="all">전체</option>
                    <option value="ongoing">연재중</option>
                    <option value="completed">완결</option>
                  </select>
                </div>
              </div>
            </div>
            <div className={styles.contentGrid}>
              {getCurrentPageData().map((item, index) => (
                <div key={item.title} className={styles.contentCard}>
                  <Content
                    title={item.title}
                    coverImg={item.coverImg}
                    summary={item.summary}
                    genre={item.genre}
                    adultContent={item.adultContent}
                    openNovelDetail={() => openContentDetail(item)}
                  />
                </div>
              ))}
            </div>
            <div className={styles.pagination}>
              <button
                className={styles.pageButton}
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage === 1}
              >
                ‹
              </button>
              {getPageNumbers().map((pageNum) => (
                <button
                  key={pageNum}
                  className={`${styles.pageButton} ${pageNum === currentPage ? styles.activePage : ''}`}
                  onClick={() => handlePageChange(pageNum)}
                >
                  {pageNum}
                </button>
              ))}
              <button
                className={styles.pageButton}
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
              >
                ›
              </button>
            </div>
          </section>

          {/* 최신 업데이트 섹션 */}
          <section className={styles.latestSection}>
            <h2>최신 업데이트</h2>
            <div className={styles.contentGrid}>
              {/* 최신 콘텐츠 컴포넌트 추가 예정 */}
            </div>
          </section>

          {/* 추천 콘텐츠 섹션 */}
          <section className={styles.recommendedSection}>
            <h2>추천 콘텐츠</h2>
            <div className={styles.contentGrid}>
              {/* 추천 콘텐츠 컴포넌트 추가 예정 */}
            </div>
          </section>

          {/* 퀵메뉴 */}
          <div className={styles.quickMenu}>
            <a href="#popular" className={styles.quickMenuItem}>
              <i className="fas fa-fire"></i>
              <span>인기</span>
            </a>
            <a href="#latest" className={styles.quickMenuItem}>
              <i className="fas fa-clock"></i>
              <span>최신</span>
            </a>
            <a href="#recommended" className={styles.quickMenuItem}>
              <i className="fas fa-star"></i>
              <span>추천</span>
            </a>
            <button onClick={scrollToTop} className={styles.quickMenuItem}>
              <i className="fas fa-arrow-up"></i>
              <span>TOP</span>
            </button>
          </div>

          {/* 푸터 섹션 */}
          <footer className={styles.footer}>
            <div className={styles.footerContent}>
              <div className={styles.footerSection}>
                <h3>서비스 정보</h3>
                <ul>
                  <li>이용약관</li>
                  <li>개인정보처리방침</li>
                  <li>고객센터</li>
                </ul>
              </div>
              <div className={styles.footerSection}>
                <h3>회사 정보</h3>
                <ul>
                  <li>회사 소개</li>
                  <li>채용 정보</li>
                  <li>제휴 문의</li>
                </ul>
              </div>
            </div>
          </footer>
        </>
      )}
    </div>
  );
}

export default ContentHome;