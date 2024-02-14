    import React, { useEffect, useState } from "react";
    import axios from "axios";
    import styles from "./NovelHome.module.css";
    import Novel from "../components/Novel";
    import NovelDetail from "../components/NovelDetail";
    import GenreButtons from "../components/GenreButtons";
    import Search from "../components/Search";
    import useDidMountEffect from "../components/useDidMountEffect"

    function NovelHome() {
      const [loading, setLoading] = useState(true);
      const [data, setDate] = useState([]);
      const [selectedNovel, setSelectedNovel] = useState(null);
      const [selectedGenre, setSelectedGenre] = useState("전체");
      const [searchInput, setSearchInput] = useState("");

      useEffect(() => {
        //const apiUrl = selectedGenre !== "전체" ? `/novel?genre=${selectedGenre}` : "/novel";
        axios
          .get("/novel", { params: { genre: selectedGenre } })
          .then((res) => {
            setDate(res.data);
            setLoading(false);
          })
          .catch((err) => console.log(err));
      }, [selectedGenre]);

      useDidMountEffect(() => {
        const encodedTitle = encodeURIComponent(searchInput);
        //const apiUrl = `/novel/search?title=${encodedTitle}`;
            axios
              .get(`/novel/search`, { params: { title: encodedTitle }}, {headers: {'content-type': 'application/json'}})
              .then((res) => {
                setDate(res.data);
              })
              .catch((err) => console.log(err));
      }, [searchInput]);


      useEffect(() => {
        document.body.style.overflow = selectedNovel ? "hidden" : "unset";
      }, [selectedNovel]);

      const openNovelDetail = (novel) => {
        document.body.style.background = "rgba(0, 0, 0, 0.8)";
        console.log(novel);
        setSelectedNovel(novel);
      };

      const closeNovelDetail = () => {
        document.body.style.background = "rgba(0, 0, 0, 0)";
        setSelectedNovel(null);
      };

      const handleGenreClick = (genre) => {
        setSelectedGenre(genre);
      };

    const handleTitleInput = (inputTitle) => {
        setSearchInput(inputTitle);
      };
      return (
        <div className={`${styles.container} ${selectedNovel != null ? styles.blurred : ""}`}>
          {loading ? (
            <div className={styles.loader}>
              <span>Loading...</span>
            </div>
          ) : (
            <>
              <div className={`${styles.header} ${selectedNovel != null ? styles.blurred : ""}`}>
                <GenreButtons selectedGenre={selectedGenre} handleGenreClick={handleGenreClick} />
                <Search handleTitleInput={handleTitleInput}/>
              </div>
              <div className={`${styles.novels} ${selectedNovel != null ? styles.blurred : ""}`}>
                {data.map((item, index) => (
                  <Novel
                    key={item.title}
                    title={item.title}
                    coverImg={item.coverImg}
                    summary={item.summary}
                    genre={item.genre}
                    adultContent={item.adultContent}
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
