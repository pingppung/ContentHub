import React, { useState } from "react";
import PropTypes from "prop-types";
import styles from "./Novel.module.css";

function Novel({ title, coverImg, summary, genre, openNovelDetail }) {
  const handleClick = () => {
    openNovelDetail({ title, coverImg, summary, genre });
  };
  return (
    <div className={styles.novel} onClick={handleClick}>
      <div className={styles.img__container}>
        <img src={coverImg} alt={title} className={styles.novel__img} />
      </div>
      <h2 className={styles.novel__title}>{title}</h2>
    </div>
  );
}

Novel.propTypes = {
  title: PropTypes.string.isRequired,
  coverImg: PropTypes.string.isRequired,
  summary: PropTypes.string.isRequired,
  genre: PropTypes.string.isRequired,
  openNovelDetail: PropTypes.func.isRequired,
};

export default Novel;
