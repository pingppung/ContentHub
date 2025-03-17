import React, { useEffect, useState } from "react";
import { useParams , useLocation, useNavigate } from 'react-router-dom';
import axios from "axios";
import Content from "../components/Content";
import GenreButtons from "../components/GenreButtons";
import Search from "../components/Search";
import { getContentData, getContentBySearch } from "../services/ContentAPIService";
import styles from "./css/NovelHome.module.css";
import Header from "../components/Header";

function ContentHome() {

  const { category, genre, title } = useParams();
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState("전체");
  const [searchInput, setSearchInput] = useState("");
  const navigate = useNavigate();
  const params = useParams();
  const location = useLocation();
  useEffect(() => {
    const category = params.category;
    //const apiUrl = selectedGenre !== "전체" ? `/novel?genre=${selectedGenre}` : "/novel";
    
    const encodedTitle = encodeURIComponent(searchInput);
    getContentBySearch(category, selectedGenre, searchInput).then((res) => {
      setDate(res);
      console.log(res);
      setLoading(false);
    });
  }, [category, selectedGenre, searchInput]);


  const openContentDetail = (content) => {
    console.log(content)
    const query = new URLSearchParams({
      title: content.title,
    }).toString();
    navigate(`/content/${category}/detail/${query}`, { state: { background: location, content } });

  };

  const handleGenreClick = (genre) => {
    setSelectedGenre(genre);
  };

  const handleTitleInput = (inputTitle) => {
    setSearchInput(inputTitle);
  };
  return (
    <div>
      <Header />
      {loading ? (
        <div className={styles.loader}>
          <span>Loading...</span>
        </div>
      ) : (
        <>
          <div>
            <Search handleTitleInput={handleTitleInput} />
            <GenreButtons
              selectedGenre={selectedGenre}
              handleGenreClick={handleGenreClick}
            />
          </div>
          <div>
            {data.map((item, index) => (
              <Content
                key={item.title}
                title={item.title}
                coverImg={item.coverImg}
                summary={item.summary}
                genre={item.genre}
                adultContent={item.adultContent}
                openNovelDetail={() => openContentDetail(item)}
              />
            ))}
          </div>
        </>
      )}

    </div>
  );
}

export default ContentHome;

// {selectedNovel != null && (
//   <NovelDetail
//     novelInfo={selectedNovel}
//     open={true}
//     close={closeNovelDetail}
//   />
// )}