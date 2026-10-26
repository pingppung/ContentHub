import React, { useState } from "react";
import styles from "../css/Search.module.css";

function Search({ handleTitleInput, variant }) {
  const [input, setInput] = useState("");
  const handleInputChange = (event) => {
    setInput(event.target.value);
  };
  return (
    <div className={`${styles.search} ${variant === 'discovery' ? styles.discovery : ''}`}>
      <input
        type="text"
        placeholder={variant === 'discovery' ? '어떤 이야기를 찾고 있나요? 작품명을 검색해보세요' : '검색어 입력'}
        aria-label="작품명 검색"
        value={input}
        onChange={handleInputChange}
      />
      <button type="button" aria-label="검색"
        onClick={() => handleTitleInput(input)}
      >검색</button>
    </div>
  );
}
export default Search;
