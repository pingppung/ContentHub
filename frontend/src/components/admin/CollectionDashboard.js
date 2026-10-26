import React, { useState } from "react";
import { platformIcons } from "../../enum/PlatformIcons";
import '../../css/CrawlButton.css';
import platforms from "../../enum/platforms";
import categories from "../../enum/categories";

function CollectionDashboard() {
  const [isLoading, setIsLoading] = useState(false);
  const [selectedPlatform, setSelectedPlatform] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
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
      await fetch('/admin/collect', {
        method: "POST",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          platform: [selectedPlatformName],
          category: [selectedCategory]
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

export default CollectionDashboard;
