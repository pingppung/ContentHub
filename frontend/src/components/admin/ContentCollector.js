import React, { useState, useEffect } from "react";
import { platformIcons } from "../../enum/PlatformIcons";
import '../../css/CrawlButton.css';

function ContentCollector() {
  const [isLoading, setIsLoading] = useState(false);
  const [selectedPlatform, setSelectedPlatform] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [isFullWidth, setIsFullWidth] = useState(window.innerWidth <= 1255);

  useEffect(() => {
    const handleResize = () => {
      setIsFullWidth(window.innerWidth <= 1255);
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const platforms = [
    { id: 'wavve', name: '웨이브', categories: ['movie', 'drama'] },
    { id: 'netflix', name: '넷플릭스', categories: ['movie', 'drama', 'animation'] },
    { id: 'disney', name: '디즈니+', categories: ['movie', 'drama', 'animation'] },
    { id: 'kakaopage', name: '카카오페이지', categories: ['webtoon', 'novel'] },
    { id: 'naverseries', name: '네이버시리즈', categories: ['webtoon', 'novel'] },
    { id: 'naverwebtoon', name: '네이버웹툰', categories: ['webtoon'] }
  ];

  const categories = [
    { id: 'movie', name: '영화' },
    { id: 'drama', name: '드라마' },
    { id: 'animation', name: '애니메이션' },
    { id: 'webtoon', name: '웹툰' },
    { id: 'novel', name: '소설' }
  ];

  const handlePlatformChange = (platformId) => {
    setSelectedPlatform(platformId);
    setSelectedCategory(''); // 플랫폼이 변경되면 카테고리 선택 초기화
  };

  const handleCategoryChange = (categoryId) => {
    setSelectedCategory(categoryId);
  };

  // 선택된 플랫폼의 카테고리만 필터링
  const availableCategories = selectedPlatform
    ? categories.filter(category =>
      platforms.find(p => p.id === selectedPlatform)?.categories.includes(category.id)
    )
    : [];

  const handleStartCollection = async () => {
    setIsLoading(true);
    try {
      const selectedPlatformName = platforms.find(p => p.id === selectedPlatform)?.name;
      const response = await fetch('/api/v1/collect', {
        method: "POST",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          platforms: [selectedPlatformName],
          categories: [selectedCategory]
        })
      });
      alert("수집 완료되었습니다!");
    } catch (error) {
      console.error("수집 중 오류 발생:", error);
      alert("수집 실패했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className={`dashboard-card full-width`}>
        <h3>콘텐츠 통계</h3>
        <div className="stats-grid">
          <div className="stat-item">
            <span className="stat-label">전체 콘텐츠</span>
            <span className="stat-value">1,234</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">이번 주 신규</span>
            <span className="stat-value">123</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">평균 조회수</span>
            <span className="stat-value">5,678</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">평균 좋아요</span>
            <span className="stat-value">234</span>
          </div>
        </div>
      </div>

      <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
        <h3>플랫폼별 콘텐츠</h3>
        <div className="platform-stats">
          <div className="platform-item">
            <span className="platform-label">Netflix</span>
            <span className="platform-value">45%</span>
          </div>
          <div className="platform-item">
            <span className="platform-label">Naver</span>
            <span className="platform-value">30%</span>
          </div>
          <div className="platform-item">
            <span className="platform-label">Wavve</span>
            <span className="platform-value">25%</span>
          </div>
        </div>
      </div>

      <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
        <h3>카테고리별 콘텐츠</h3>
        <div className="category-stats">
          <div className="category-item">
            <span className="category-label">웹툰</span>
            <span className="category-value">40%</span>
          </div>
          <div className="category-item">
            <span className="category-label">영화</span>
            <span className="category-value">25%</span>
          </div>
          <div className="category-item">
            <span className="category-label">드라마</span>
            <span className="category-value">20%</span>
          </div>
          <div className="category-item">
            <span className="category-label">소설</span>
            <span className="category-value">15%</span>
          </div>
        </div>
      </div>

      <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
        <h3>최근 업로드된 콘텐츠</h3>
        <div className="recent-content">
          {/* 여기에 최근 콘텐츠 목록을 표시할 수 있습니다 */}
        </div>
      </div>
      <div className={`dashboard-card full-width`}>
        <div className="content-collector">
          <h2>콘텐츠 수집</h2>
          <div className="collector-grid">
            <div className="selection-section">
              <h3>플랫폼 선택</h3>
              <div className="platform-grid">
                {platforms.map(platform => (
                  <div
                    key={platform.id}
                    className={`platform-card ${selectedPlatform === platform.id ? 'selected' : ''}`}
                    onClick={() => handlePlatformChange(platform.id)}
                  >
                    <div className="platform-icon">
                      <img src={platformIcons[platform.name]} alt={`${platform} 아이콘`}/>
                    </div>
                    <span className="platform-name">{platform.name}</span>
                    <span className="platform-categories">
                      {platform.categories.map(catId =>
                        categories.find(c => c.id === catId)?.name
                      ).join(', ')}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            <div className="selection-section">
              <h3>카테고리 선택</h3>
              <div className="category-grid">
                {availableCategories.map(category => (
                  <div
                    key={category.id}
                    className={`category-card ${selectedCategory === category.id ? 'selected' : ''}`}
                    onClick={() => handleCategoryChange(category.id)}
                  >
                    <span className="category-name">{category.name}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <button
            onClick={handleStartCollection}
            className={`crawl-button ${isLoading ? "loading" : ""}`}
            disabled={isLoading || !selectedPlatform || !selectedCategory}
          >
            {isLoading ? "데이터 수집 중..." : "데이터 수집 시작"}
          </button>
        </div>
      </div>
    </>
  );
}

export default ContentCollector;
