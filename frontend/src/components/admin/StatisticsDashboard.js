import React, { useState, useEffect } from "react";
import AdminService from "../../services/AdminStatisticsService";
import ChartCard from "./ChartCard";
import categories from "../../enum/categories";

function StatisticsDashboard() {
    const [categoryStats, setCategoryStats] = useState([]);
    const [userGrowth, setUserGrowth] = useState([]);
    const [isFullWidth, setIsFullWidth] = useState(false);


    useEffect(() => {
        const handleResize = () => {
            setIsFullWidth(window.innerWidth <= 1255);
        };

        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);


    useEffect(() => {
        const statsContent = async () => {
            try {
                const res = await AdminService.getContentCount();
                const displayCategoryStats = translateCategoryField(res.categoryCounts, categories);
                setCategoryStats(displayCategoryStats);

                setUserGrowth(res.userGrowth);
            } catch (error) {
                console.error("Error fetching category stats:", error);
            }
        };
        statsContent();
    }, []);

    const translateCategoryField = (items, categories) => {
        return items.map(item => {
            const category = categories.find(cat => cat.id === item.field);
            return {
                ...item,
                field: category ? category.name : item.field
            };
        });
    };

    return (
        <>
            <div className={`dashboard-card full-width`}>
                <h3>콘텐츠 통계</h3>
                <div className="stats-grid">
                    <div className="stat-item">
                        <span className="stat-label">전체 콘텐츠</span>
                        <span className="stat-value">553</span>
                    </div>
                    <div className="stat-item">
                        <span className="stat-label">이번 주 신규</span>
                        <span className="stat-value">120</span>
                    </div>
                    <div className="stat-item">
                        <span className="stat-label">평균 조회수</span>
                        <span className="stat-value">0</span>
                    </div>
                    <div className="stat-item">
                        <span className="stat-label">평균 좋아요</span>
                        <span className="stat-value">1</span>
                    </div>
                </div>
            </div>

            <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
                <h3>플랫폼별 콘텐츠</h3>
                <div className="platform-stats">
                    <div className="platform-item">
                        <span className="platform-label">KaKao</span>
                        <span className="platform-value">50.9%</span>
                    </div>
                    <div className="platform-item">
                        <span className="platform-label">Naver</span>
                        <span className="platform-value">3.6%</span>
                    </div>
                    <div className="platform-item">
                        <span className="platform-label">Wavve</span>
                        <span className="platform-value">20%</span>
                    </div>
                    <div className="platform-item">
                        <span className="platform-label">Netflix</span>
                        <span className="platform-value">25.5%</span>
                    </div>
                </div>
            </div>

            <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
                <ChartCard
                    title="카테고리별 콘텐츠"
                    data={categoryStats}
                    type="pie"
                    xKey="field"
                />
            </div>
            <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
                <ChartCard
                    title="유저"
                    data={userGrowth}
                    type="bar"
                    xKey="date"
                />
            </div>


            <div className={`dashboard-card ${isFullWidth ? 'full-width' : ''}`}>
                <h3>최근 업로드된 콘텐츠</h3>
                <div className="recent-content">
                    {/* 여기에 최근 콘텐츠 목록을 표시할 수 있습니다 */}
                </div>
            </div>
        </>
    );
}

export default StatisticsDashboard;
