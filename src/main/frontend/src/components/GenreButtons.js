import React from "react";


function GenreButtons({ selectedGenre, handleGenreClick }) {
  const genres = ["전체", "로맨스", "로판", "판타지", "현판", "무협", "미스터리", "라이트노벨", "BL", "드라마"];

  return (
    <div className={"genreButtons"}>
      {genres.map((genre) => (
        <button
          key={genre}
          className={genre}
          onClick={() => handleGenreClick(genre)}>
          {genre}
        </button>
      ))}
    </div>
  );
}

export default GenreButtons;
