import React, { useState } from "react";
import './css/CrawlButton.css';

function CrawlButton() {
  const [isLoading, setIsLoading] = useState(false);

  const handleCrawl = async () => {
    setIsLoading(true); // 로딩 상태 표시
    try {
      const response = await fetch("/api/crawler", { method: "POST" }); // API 호출
      alert("크롤링이 완료되었습니다!");
    } catch (error) {
      console.error("크롤링 중 오류 발생:", error);
      alert("크롤링에 실패했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <button 
      onClick={handleCrawl} 
      disabled={isLoading}
      className={`crawl-button ${isLoading ? "loading" : ""}`}
    >
      {isLoading ? "크롤링 중..." : "크롤링 실행"}
    </button>
  );
}

export default CrawlButton;
