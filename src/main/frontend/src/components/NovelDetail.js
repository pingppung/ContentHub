import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import styles from "./NovelDetail.module.css";

function NovelDetail({ novelInfo, open, close }) {
  const [novelId, setNovelId] = useState("");

  useEffect(() => {
    setNovelId(novelInfo.id);
  }, [novelInfo]);
  return (
    <div className={styles.novel__detail}>
      {open ? (
        <section>
          <header>
            {novelInfo.title}
          </header>

          <div className={styles.novel__content}>
            {novelInfo.genre} <br/>
            {novelInfo.summary}
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
};

NovelDetail.propTypes = {
  novelInfo: PropTypes.shape({
      id: PropTypes.string.isRequired,
      coverImg: PropTypes.string.isRequired,
      title: PropTypes.string.isRequired,
      summary: PropTypes.string.isRequired,
      genre: PropTypes.string.isRequired,
    }).isRequired,
  open: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
};

export default NovelDetail;
