import React, { useState } from "react";
import PropTypes from "prop-types";
import styles from "./Novel.module.css";
import NovelDetail from "./NovelDetail";

function Novel({ id, coverImg, title, summary, genre }) {
  const [novelDetailOpen, setNovelDetailOpen] = useState(false);

  const openModal = () => {
    setNovelDetailOpen(true);
  };

  const closeModal = () => {
    setNovelDetailOpen(false);
  };

  return (
    <div className={styles.novel__container}>
      <NovelDetail
        id={id}
        open={novelDetailOpen}
        close={closeModal}
      />
      <div className={styles.novel} onClick={openModal}>
        <img src={coverImg} alt={title} className={styles.novel__img} />
        <div>
          <h2 className={styles.novel__title}>{title}</h2>
          <h4 className={styles.novel__genre}>{genre}</h4>
          <p>
            {summary.length > 235 ? `${summary.slice(0, 235)}...` : summary}
          </p>
        </div>
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
};

export default Novel;
