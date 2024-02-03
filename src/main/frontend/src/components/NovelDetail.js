import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import styles from "./NovelDetail.module.css";

function NovelDetail({ id, open, close }) {
  const [novelId, setNovelId] = useState("");

  useEffect(() => {
    setNovelId(id);
  }, [id]);

  return (
    <div className={open ? styles.novel__detail : styles.novel__close__detail}>
      {open ? (
        <section>
          <header>
            소설 정보
          </header>

          <div className={styles.novel__content}>{novelId}</div>

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
  id: PropTypes.string.isRequired,
  open: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
};

export default NovelDetail;
