import { AiFillHeart, AiOutlineHeart } from "react-icons/ai";
import styles from "../css/LikeButton.module.css"

function LikeButton({ liked, onToggle, count }) {
    return (
        <div className={styles.likeSection}>
            <button
                className={`${styles.likeButton} ${liked ? styles.liked : ""}`}
                onClick={onToggle}
            >
                <span className={styles.icon}>
                    {liked ? <AiFillHeart size={24} /> : <AiOutlineHeart size={24} />}
                </span>
            </button>
        </div>
    );
}

export default LikeButton;