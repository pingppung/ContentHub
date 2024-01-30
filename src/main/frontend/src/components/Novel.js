import PropTypes from "prop-types";
import styles from "./Novel.module.css";

function Novel({coverImg, title, summary, genre}) {
  return (
    <div className={styles.novel}>
      <img src={coverImg} alt={title} className={styles.novel__img} />
      <div>
        <h2 className={styles.novel__title}>
          {title}
        </h2>
        <h4 className={styles.novel__genre}>
          {genre}
        </h4>
        <p>{summary.length > 235 ? `${summary.slice(0, 235)}...` : summary}</p>
      </div>
    </div>
  );
}
Novel.prototype = {
  coverImg: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  summary: PropTypes.string.isRequired,
  genre: PropTypes.string.isRequired,
};
export default Novel;
