import React, { useEffect, useRef } from "react";
import PropTypes from "prop-types";
import styles from "./NovelDetail.module.css";

function NovelDetail({ novelInfo, open, close }) {
  const modalRef = useRef();

  useEffect(() => {
    document.addEventListener("mousedown", clickModalOutside);

    return () => {
      document.removeEventListener("mousedown", clickModalOutside);
    };
  }, [modalRef]);
  const clickModalOutside = (e) => {
    if (!modalRef.current.contains(e.target)) {
      close();
    }
  };
  return (
    <div ref={modalRef} className={styles.novel__detail}>
      {open ? (
        <section>
          <header>
            <h2 className={styles.novel__title}>{novelInfo.title}
          </h2></header>

          <div className={styles.novel__content}>
            <img src={novelInfo.coverImg} alt={novelInfo.title} className={styles.novel__img} />
            <div>
              <h4 className={styles.novel__genre}>{novelInfo.genre}</h4>
              <p>{novelInfo.summary}</p>
            </div>
          </div>

          <footer>
            <button className={styles.close} onClick={close}>
              close
            </button>
          </footer>
        </section>
      ) : null}
    </div>
  );
}

NovelDetail.propTypes = {
  novelInfo: PropTypes.shape({
    title: PropTypes.string.isRequired,
    coverImg: PropTypes.string.isRequired,
    summary: PropTypes.string.isRequired,
    genre: PropTypes.string.isRequired,
  }).isRequired,
  open: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
};

export default NovelDetail;
