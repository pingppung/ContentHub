import React, { useState } from "react";
import PropTypes from "prop-types";
import styles from "./Novel.module.css";

function Novel({ id, coverImg, title, summary, genre, openNovelDetail }) {
  const handleClick = () => {
    openNovelDetail({ id, coverImg, title, summary, genre });
  };
  return (
    <div className={styles.novel} onClick={handleClick}>
      <img src={coverImg} alt={title} className={styles.novel__img} />
      <div>
        <h2 className={styles.novel__title}>{title}</h2>
        <h4 className={styles.novel__genre}>{genre}</h4>
        <p>{summary.length > 235 ? `${summary.slice(0, 235)}...` : summary}</p>
      </div>
    </div>
  );
}

Novel.propTypes = {
  id: PropTypes.string.isRequired,
  coverImg: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  summary: PropTypes.string.isRequired,
  genre: PropTypes.string.isRequired,
  openNovelDetail: PropTypes.func.isRequired,
};

export default Novel;
