import React, { useState } from "react";
import '../../css/CrawlButton.css';

function ContentCollector() {
  const [isLoading, setIsLoading] = useState(false);
  const [selectedPlatforms, setSelectedPlatforms] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);

  const platforms = [
    { id: 'wavve', name: '웨이브' },
    { id: 'netflix', name: '넷플릭스' },
    { id: 'disney', name: '디즈니+' }
  ];

  const categories = [
    { id: 'movie', name: '영화' },
    { id: 'drama', name: '드라마' },
    { id: 'animation', name: '애니메이션' }
  ];

  const handlePlatformChange = (platformId) => {
    const platformName = platforms.find(p => p.id === platformId)?.name;
    if (!platformName) return;

    setSelectedPlatforms(prev => {
      if (prev.includes(platformId)) {
        return prev.filter(id => id !== platformId);
      }
      return [...prev, platformId];
    });
  };

  const handleCategoryChange = (categoryId) => {
    setSelectedCategories(prev => {
      if (prev.includes(categoryId)) {
        return prev.filter(id => id !== categoryId);
      }
      return [...prev, categoryId];
    });
  };

  const handleStartCollection = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('/api/v1/collect', {
        method: "POST",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          platforms: selectedPlatforms,
          categories: selectedCategories
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
      <div className="dashboard-card full-width">
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

      <div className="dashboard-card">
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

      <div className="dashboard-card">
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

      <div className="dashboard-card">
        <div className="content-collector">
          <h2>콘텐츠 수집</h2>
          <div className="collector-grid">
            <div className="selection-section">
              <h3>플랫폼 선택</h3>
              <div className="checkbox-group">
                {platforms.map(platform => (
                  <label key={platform.id} className="checkbox-label">
                    <input
                      type="checkbox"
                      checked={selectedPlatforms.includes(platform.id)}
                      onChange={() => handlePlatformChange(platform.id)}
                    />
                    <span className="checkbox-text">{platform.name}</span>
                  </label>
                ))}
              </div>
            </div>

            <div className="selection-section">
              <h3>카테고리 선택</h3>
              <div className="checkbox-group">
                {categories.map(category => (
                  <label key={category.id} className="checkbox-label">
                    <input
                      type="checkbox"
                      checked={selectedCategories.includes(category.id)}
                      onChange={() => handleCategoryChange(category.id)}
                    />
                    <span className="checkbox-text">{category.name}</span>
                  </label>
                ))}
              </div>
            </div>
          </div>

          <button
            onClick={handleStartCollection}
            className={`crawl-button ${isLoading ? "loading" : ""}`}
            disabled={isLoading || selectedPlatforms.length === 0 || selectedCategories.length === 0}
          >
            {isLoading ? "데이터 수집 중..." : "데이터 수집 시작"}
          </button>
        </div>
      </div>
      <div className="dashboard-card">
        <h3>최근 업로드된 콘텐츠</h3>
        <div className="recent-content">
          {/* 여기에 최근 콘텐츠 목록을 표시할 수 있습니다 */}
        </div>
      </div>
    </>
  );
}

export default ContentCollector;
