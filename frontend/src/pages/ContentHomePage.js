import React, { useEffect, useRef, useState } from "react";
import { useParams, useLocation, useNavigate, Link } from 'react-router-dom';
import Content from "../components/content/Content";
import GenreButtons from "../components/common/GenreButtons";
import Search from "../components/common/Search";
import PopularContent from "../components/content/PopularContent";
import { getContentBySearch } from "../services/ContentAPIService";
import styles from "./css/ContentHome.module.css";
import Header from "../components/common/Header";

function ContentHome() {
  const { category } = useParams();
  return <ContentHomeContent key={category || "all"} category={category} />;
}

function ContentHomeContent({ category }) {
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [popularContent, setPopularContent] = useState([]);
  const [loadError, setLoadError] = useState(false);
  const [retryCount, setRetryCount] = useState(0);
  const initialContents = useRef(null);
  const [selectedGenre, setSelectedGenre] = useState("전체");
  const [searchInput, setSearchInput] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 105;
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    let active = true;
    setLoading(true);
    setLoadError(false);

    // 기본 목록은 인기 영역과 공유하고 필터 변경 시 다시 요청하지 않는다.
    if (!initialContents.current) {
      initialContents.current = getContentBySearch(category, "전체", "");
    }
    initialContents.current.then((res) => {
      if (active) setPopularContent(Array.isArray(res) ? res.slice(0, 5) : []);
    });

    const request = selectedGenre === "전체" && !searchInput
      ? initialContents.current
      : getContentBySearch(category, selectedGenre, searchInput);
    request.then((res) => {
      if (!active) return;
      setLoadError(!Array.isArray(res));
      setData(Array.isArray(res) ? res : []);
      setLoading(false);
    });

    return () => { active = false; };
  }, [category, selectedGenre, searchInput, retryCount]);

  const retryLoad = () => {
    initialContents.current = null;
    setRetryCount(count => count + 1);
  };

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
    const contentCategory = content.type?.toLowerCase() || category;
    const query = new URLSearchParams({
      title: content.title,
    }).toString();
    navigate(`/content/${contentCategory}/detail/${query}`, { state: { background: location, content, category: contentCategory } });

  };

  const handleGenreClick = (genre) => {
    setSelectedGenre(genre);
    setCurrentPage(1);
  };

  const handleTitleInput = (inputTitle) => {
    setSearchInput(inputTitle.trim());
    setCurrentPage(1);
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

  const categoryLabels = { webtoon: "웹툰", novel: "웹소설", movie: "영화", drama: "드라마" };
  return (
    <div className={styles.container}>
      <Header variant="discovery" />
      {loading ? <div className={styles.loader} role="status">이야기를 불러오고 있어요…</div> : <>
        <section className={styles.hero} aria-labelledby="discovery-title">
          <div className={styles.heroArtwork}><PopularContent contents={popularContent} variant="hero" /></div>
          <div className={styles.heroCopy}>
            <p className={styles.eyebrow}>웹툰, 웹소설, 영화, 드라마를 한 곳에서</p>
            <h1 id="discovery-title">오늘 뭐 <span>볼까?</span></h1>
            <p className={styles.heroDescription}>좋아하는 이야기가 더 가까워지는 곳,<br />다양한 콘텐츠를 발견하고 공식 플랫폼에서 만나보세요.</p>
            <Search handleTitleInput={handleTitleInput} variant="discovery" />
            <nav className={styles.categoryTabs} aria-label="콘텐츠 카테고리">
              {[["webtoon", "웹툰"], ["novel", "웹소설"], ["movie", "영화"], ["drama", "드라마"]].map(([id,label]) =>
                <Link key={id} to={"/contents/" + id} className={category === id ? styles.selectedCategory : ""} aria-current={category === id ? "page" : undefined}>{label}</Link>)}
            </nav>
          </div>
        </section>
        <main className={styles.pageContent}>
          {loadError && <div role="alert">
            <p>콘텐츠를 불러오지 못했습니다. 서버 연결을 확인하고 다시 시도해 주세요.</p>
            <button type="button" onClick={retryLoad}>다시 시도</button>
          </div>}
          <section id="popular" className={styles.popularSection}>
            <div className={styles.contentHeader}><h2>지금 인기 있는 콘텐츠</h2><a className={styles.moreLink} href="#all-content">더보기 ›</a></div>
            <div className={styles.popularGrid}>{popularContent.map(item =>
              <Content key={item.title} {...item} variant="discovery" categoryLabel={category ? undefined : categoryLabels[item.type?.toLowerCase()]} openNovelDetail={() => openContentDetail(item)} />)}</div>
          </section>
          <div className={styles.genreSection}><GenreButtons category={category} selectedGenre={selectedGenre} handleGenreClick={handleGenreClick} /></div>
          <section id="all-content" className={styles.mainContentSection}>
            <div className={styles.contentHeader}><h2>전체 콘텐츠</h2>
              <div className={styles.contentFilters}>
                <label className={styles.filterGroup}>정렬:
                  <select className={styles.filterSelect}><option value="latest">최신순</option><option value="popular">인기순</option><option value="rating">평점순</option></select>
                </label>
                <label className={styles.filterGroup}>상태:
                  <select className={styles.filterSelect}><option value="all">전체</option><option value="ongoing">연재중</option><option value="completed">완결</option></select>
                </label>
              </div>
            </div>
            <div className={styles.contentGrid}>{getCurrentPageData().map(item =>
              <Content key={item.title} {...item} variant="discovery" categoryLabel={category ? undefined : categoryLabels[item.type?.toLowerCase()]} openNovelDetail={() => openContentDetail(item)} />)}</div>
            <div className={styles.pagination} aria-label="콘텐츠 페이지">
              <button className={styles.pageButton} onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} aria-label="이전 페이지">‹</button>
              {getPageNumbers().map(pageNum => <button key={pageNum} className={styles.pageButton + " " + (pageNum === currentPage ? styles.activePage : "")} onClick={() => handlePageChange(pageNum)} aria-current={pageNum === currentPage ? "page" : undefined}>{pageNum}</button>)}
              <button className={styles.pageButton} onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages} aria-label="다음 페이지">›</button>
            </div>
          </section>
          <section id="latest" className={styles.latestSection}>
            <h2>하나의 이야기가<br />다양한 모습으로</h2><p>좋아하는 작품이 웹소설, 웹툰, 드라마, 영화로 이어지는<br />특별한 경험을 만나보세요.</p><a href="#all-content">다음 이야기 찾아보기 →</a>
          </section>
          <section id="recommended" className={styles.recommendedSection}><h2>추천 콘텐츠</h2><p>좋아하는 작품의 상세 페이지에서 관련 콘텐츠를 만나보세요.</p></section>
        </main>
        <div className={styles.quickMenu}>
          <a href="#popular" className={styles.quickMenuItem}>인기</a><a href="#latest" className={styles.quickMenuItem}>최신</a><a href="#recommended" className={styles.quickMenuItem}>추천</a><button onClick={scrollToTop} className={styles.quickMenuItem}>↑ TOP</button>
        </div>
        <footer className={styles.footer}><div className={styles.footerContent}>
          <div className={styles.footerSection}><h3>서비스 정보</h3><ul><li>이용약관</li><li>개인정보처리방침</li><li>고객센터</li></ul></div>
          <div className={styles.footerSection}><h3>회사 정보</h3><ul><li>회사 소개</li><li>채용 정보</li><li>제휴 문의</li></ul></div>
        </div></footer>
      </>}
    </div>
  );
}
export default ContentHome;
