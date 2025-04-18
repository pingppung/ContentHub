import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import { platformIcons } from "../enum/PlatformIcons";
import UserActivityService from '../services/UserActivityService';
import styles from "./css/ContentDetail.module.css";
import AuthService from "../services/AuthService";
import ContentOptions from "../components/ContentOptions";

function ContentDetail() {
	const [liked, setLiked] = useState(false);
	const [count, setCount] = useState();
	const [expanded, setExpanded] = useState(false);
	const navigate = useNavigate();
	const location = useLocation();
	const { background, content, category } = location.state || {};
	const isLongText = content.description.length > 100;
	useEffect(() => {
		document.body.style.overflow = 'hidden';
		const loadLikeStatus = async () => {
			try {
				const token = AuthService.getToken();
				const likeStatus = await UserActivityService.getLikeStatus(content.title, category, token);
				setLiked(likeStatus);
			} catch (error) {
				console.error('Failed to fetch like status:', error);
			}
		};
		loadLikeStatus();
	}, []);

	const getIconForSite = (platform) => {
		const iconUrl = platformIcons[platform] || platformIcons['기타'];
		return <img src={iconUrl} alt={`${platform} 아이콘`} style={{ width: '30px', height: '30px' }} />;
	};

	const handleLikeToggle = async () => {
		setLiked((prev) => !prev);
	};

	// const sendLikeStatus = async () => {
	// 	try {
	// 		if (liked) {
	// 			await removeLike(contentId, category);
	// 		} else {
	// 			await addLike(contentId, category);
	// 		}
	// 		setLiked(!liked); // 상태 변경

	// 		setCount(prevCount => (liked ? prevCount - 1 : prevCount + 1));
	// 	} catch (error) {
	// 		console.error('Failed to toggle like:', error);
	// 	}
	// };

	const handleClose = () => {
		//sendLikeStatus();
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
					{/* 이미지 */}
					<div className={styles.imageContainer}>
						<img src={content.coverImg} alt={content.title} className={styles.coverImg} />
					</div>
					{/* 텍스트 부분 */}
					<div className={styles.contentContainer}>
						<h2 className={styles.contentTitle}>{content.title}</h2>
						<p className={styles.contentGenre}>
							태그<span className={styles.spacing}>#{content.genre} #ㄴㅁㅇㄹ</span>
						</p>
						<p className={styles.contentGenre}>
							화별<span className={styles.spacing}>총 00화 / 완결or미완결</span>
						</p>
						{/* 외부 링크들 */}
						<div className={styles.linkButtons}>
							연재 플랫폼<span className={styles.spacing}>
								{content.links.map((item, index) => (
									<div key={index} className={styles.platform} onClick={() => redirectToPlatform(item.url)}>
										{getIconForSite(item.platform)}
										{item.platform}
									</div>
								))}
							</span>
						</div>

					</div>


				</div>
				<p className={styles.contentDescription}>
					{expanded || !isLongText ? content.description : `${content.description.substring(0, 100)}...`}
					{isLongText && (
						<button onClick={() => setExpanded(!expanded)} className={styles.toggleButton}>
							{expanded ? "접기" : "더보기"}
						</button>
					)}
				</p>
				<ContentOptions/>



			</div>

		</div >
	);
}

export default ContentDetail;