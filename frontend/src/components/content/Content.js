import React from "react";
import styles from "../css/Content.module.css";

function Content({ title, coverImg, synopsis, genre, openNovelDetail, variant, categoryLabel, links = [] }) {

  const handleClick = () => {
    openNovelDetail({ title, coverImg, synopsis, genre});
  };
  return (
    <div className={`${styles.novel} ${variant === 'discovery' ? styles.discovery : ''}`} onClick={handleClick}
      role="button" tabIndex={0} onKeyDown={(event) => {
        if (event.key === 'Enter' || event.key === ' ') {
          event.preventDefault();
          handleClick();
        }
      }}>
      <div className={styles.img__container}>
        <img src={coverImg} alt={title} className={styles.novel__img} />
        {variant === 'discovery' && categoryLabel && <span className={styles.categoryTag}>{categoryLabel}</span>}
      </div>
      <h2 className={styles.novel__title}>
        {title}
      </h2>
      {variant === 'discovery' && <div className={styles.metadata}>
        <p>{synopsis || '새로운 이야기를 만나보세요.'}</p>
        <span className={styles.platform}>{links.map(link => link.platform).join(' · ') || '콘텐츠 둘러보기'}</span>
      </div>}
    </div>
  );
}

export default Content;
