import React, { useState } from "react";
import axios from "axios";
import styles from "./Search.module.css";

function Search({ handleTitleInput}) {
    const [input, setInput] = useState("");
  const handleInputChange = (event) => {
    setInput(event.target.value);
  };
  return (
    <div className={styles.search}>
      <input
        type="text"
        placeholder="검색어 입력"
        value={input}
        onChange={handleInputChange}
      />
      <img
        src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png"
        onClick={() => handleTitleInput(input)}/>
    </div>
  );
}
export default Search;
