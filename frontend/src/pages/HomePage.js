import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import styles from "./css/Home.module.css";

function Home() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(false);
  }, []);

  return (
    <div className={styles.relactive}>
      <Header />
      {loading ? (
        <div className={"loader"}>
          <span>Loading...</span>
        </div>
      ) : (
            <div> 홈 페이지 !!! </div>
      )}
    </div>
  );
}

export default Home;
