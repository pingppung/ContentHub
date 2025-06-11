import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import LikeButton from "../components/common/LikeButton";
import { platformIcons } from "../enum/PlatformIcons";
import UserActivityService from '../services/UserActivityService';
import styles from "./css/ContentDetail.module.css";
import AuthService from "../services/AuthService";
import ContentOptions from "../components/content/ContentOptions";
import { FaHeart, FaBookmark, FaShare, FaEye } from 'react-icons/fa';

function ContentDetail() {
	const [liked, setLiked] = useState(false);
	const [saved, setSaved] = useState(false);
	const [count, setCount] = useState(0);
	const [expanded, setExpanded] = useState(false);
	const navigate = useNavigate();
	const location = useLocation();
	const { background, content, category } = location.state || {};
	const isLongText = content.description.length > 100;
	useEffect(() => {
		document.body.style.overflow = 'hidden'; // 스크롤을 막는 처리
		const loadStatus = async () => {
			try {
				const token = AuthService.getToken();
				const [likeStatus, saveStatus] = await Promise.all([
					UserActivityService.fetchLikeStatus(content.title, category, token),
					UserActivityService.fetchSaveStatus(content.title, category, token)
				]);
				setLiked(likeStatus);
				setSaved(saveStatus);
			} catch (error) {
				console.error('Failed to fetch status:', error);
			}
		};
		loadStatus();
	}, []);

	const getPlatformIconImage = (platform) => {
		const iconUrl = platformIcons[platform] || platformIcons['기타'];
		return <img src={iconUrl} alt={`${platform} 아이콘`} style={{ width: '20px', height: '20px' }} />;
	};

	const getAgeAdultIcon = (ageAdult) => {
		const ageIcon = platformIcons['청불'];
		return ageAdult ? <img src={ageIcon} style={{ width: '15px', height: '15px' }} /> : null;
	};

	const handleLikeToggle = async () => {
		setLiked((prev) => !prev);
	};

	const handleSaveToggle = async () => {
		try {
			const token = AuthService.getToken();
			if (saved) {
				await UserActivityService.removeSave(content.title, category, token);
			} else {
				await UserActivityService.addSave(content.title, category, token);
			}
			setSaved(!saved);
		} catch (error) {
			console.error('Failed to toggle save:', error);
		}
	};

	const handleShare = async () => {
		try {
			await navigator.share({
				title: content.title,
				text: content.description,
				url: window.location.href,
			});
		} catch (error) {
			console.error('Failed to share:', error);
		}
	};

	const handleClose = () => {
		document.body.style.overflow = '';
		navigate(-1);
	};

	const redirectToPlatform = (url) => {
		window.open(url, "_blank");
	}

	return (
		<div className={styles.modalOverlay}>
			<div className={styles.modalContainer}>
				{/* 배경 */}
				<div className={styles.modalBackground}></div>
				<button className={styles.closeButton} onClick={handleClose}>X</button>

				{/* 내용 */}
				<div className={styles.contentSection}>
					{/* 좌측: 커버 이미지와 좋아요 버튼 */}
					<div className={styles.coverContainer}>
						<img src={content.coverImg} alt={content.title} className={styles.coverImg} />
						<div className={styles.actionButtons}>
							<button
								className={`${styles.actionButton} ${liked ? styles.active : ''}`}
								onClick={handleLikeToggle}
							>
								<FaHeart />
								<span>좋아요</span>
							</button>
							<button
								className={`${styles.actionButton} ${saved ? styles.active : ''}`}
								onClick={handleSaveToggle}
							>
								<FaBookmark />
								<span>저장</span>
							</button>
							<button
								className={styles.actionButton}
								onClick={handleShare}
							>
								<FaShare />
								<span>공유</span>
							</button>
						</div>
					</div>

					{/* 우측: 텍스트 정보 */}
					<div className={styles.contentContainer}>
						<h2 className={styles.contentTitle}>{content.title}</h2>

						{/* 태그 */}
						<div className={styles.infoItem}>
							<span className={styles.label}>태그</span>
							<span className={styles.spacing} />
							<span className={styles.value}>#{content.genre}</span>
						</div>

						{/* 회차 */}
						<div className={styles.infoItem}>
							<span className={styles.label}>화별</span>
							<span className={styles.spacing} />
							<span className={styles.value}>총 00화 / 완결or미완결</span>
						</div>

						{/* 연재 플랫폼 */}
						<div className={styles.infoItem}>
							<span className={styles.label}>연재 플랫폼</span>
							<span className={styles.spacing} />
							<div className={styles.platforms}>
								{content.links.map((item, index) => (
									<div
										key={index}
										className={styles.platform}
										onClick={() => redirectToPlatform(item.url)}
									>
										{getPlatformIconImage(item.platform)}
										{item.platform}
										{getAgeAdultIcon(item.adult)}
									</div>
								))}
							</div>
						</div>

						{/* 줄거리 */}
						<div className={styles.infoItem}>
							<span className={styles.label}>줄거리</span>
							<span className={styles.spacing} />
							<p className={styles.value}>
								{expanded || !isLongText
									? content.description
									: `${content.description.substring(0, 100)}...`}
								{isLongText && (
									<button
										onClick={() => setExpanded(!expanded)}
										className={styles.toggleButton}
									>
										{expanded ? '접기' : '더보기'}
									</button>
								)}
							</p>
						</div>
					</div>
				</div>
				<ContentOptions />



			</div>

		</div >
	);
}

export default ContentDetail;