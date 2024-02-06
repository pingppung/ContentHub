import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./NovelHome.module.css";
import Novel from "../components/Novel";
import NovelDetail from "../components/NovelDetail";
import GenreButtons from "../components/GenreButtons";

function NovelHome() {
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);
  const [selectedNovel, setSelectedNovel] = useState(null);
  const [selectedGenre, setSelectedGenre] = useState("전체");

  useEffect(() => {
    const apiUrl = selectedGenre !== "전체" ? `/novel/${selectedGenre}` : "/novel";
    axios
      .get(apiUrl, { params: { genre: selectedGenre } })
      .then((res) => {
        setDate(res.data);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, [selectedGenre]);

  useEffect(() => {
    document.body.style.overflow = selectedNovel ? "hidden" : "unset";
  }, [selectedNovel]);

  const openNovelDetail = (novel) => {
    document.body.style.background = "rgba(0, 0, 0, 0.8)";
    setSelectedNovel(novel);
  };

  const closeNovelDetail = () => {
    document.body.style.background = "rgba(0, 0, 0, 0)";
    setSelectedNovel(null);
  };

  const handleGenreClick = (genre) => {
    setSelectedGenre(genre);
  };

  return (
    <div className={`${styles.container} ${selectedNovel != null ? styles.blurred : ""}`}>
      {loading ? (
        <div className={styles.loader}>
          <span>Loading...</span>
        </div>
      ) : (
        <>
          <GenreButtons selectedGenre={selectedGenre} handleGenreClick={handleGenreClick} />
          <div className={`${styles.novels} ${selectedNovel != null ? styles.blurred : ""}`}>
            {data.map((item, index) => (
              <Novel
                key={item.id}
                id={item.id}
                coverImg={item.coverImg}
                title={item.title}
                summary={item.summary}
                genre={item.genre}
                openNovelDetail={() => openNovelDetail(item)}
              />
            ))}
          </div>
        </>
      )}
      {selectedNovel != null && (
        <NovelDetail
          novelInfo={selectedNovel}
          open={true}
          close={closeNovelDetail}
        />
      )}
    </div>
  );
}

export default NovelHome;
