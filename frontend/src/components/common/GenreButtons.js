import React from "react";
import styles from "../css/GenreButtons.module.css";
function GenreButtons({ category, selectedGenre, handleGenreClick }) {
  const genresByCategory = {
    webtoon: ["전체", "로맨스", "로판", "판타지", "현판", "무협", "미스터리", "코미디", "액션"],
    novel: ["전체", "로맨스", "로판", "판타지", "현판", "무협", "미스터리", "스릴러", "코미디", "액션", "BL"],
    drama: ["전체", "로맨스", "스릴러", "미스터리", "코미디", "액션", "다큐멘터리", "가족", "성장"],
    movie: ["전체", "로맨스", "스릴러", "미스터리", "코미디", "액션", "다큐멘터리", "SF", "공포"],
    animation: ["전체", "로맨스", "판타지", "액션", "코미디", "SF", "성장", "BL", "무협"]
  };

  const genres = genresByCategory[category] || [];
  return (
    <div className={styles.genreButtons}>
      {genres.map((genre) => (
        <button
          key={genre}
          className={genre === selectedGenre ? styles.active : ""}
          onClick={() => handleGenreClick(genre)}>
          {genre}
        </button>
      ))}
    </div>
  );
}

export default GenreButtons;
