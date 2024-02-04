import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./NovelHome.module.css";
import Novel from "../components/Novel";
import NovelDetail from "../components/NovelDetail";

function NovelHome() {
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);
  const [selectedNovel, setSelectedNovel] = useState(null);

  useEffect(() => {
    axios
      .get("/novel")
      .then((res) => {
        setDate(res.data);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, []);

  const openNovelDetail = (novel) => {
  console.log(novel);
    setSelectedNovel(novel);
  };

  const closeNovelDetail = () => {
    setSelectedNovel(null);
  };
  return (
    <div className={styles.container}>
      {loading ? (
        <div className={styles.loader}>
          <span>Loading...</span>
        </div>
      ) : (
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
