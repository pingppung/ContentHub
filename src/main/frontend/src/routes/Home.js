import React, { useEffect, useState } from "react";
import axios from "axios";
import Header from "../components/Header";
import styles from "./Home.module.css";

function Home({ name, loggedIn, setLoggedIn }) {
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);

  useEffect(() => {
    setLoading(false);
  }, []);

  return (
    <div className={styles.relactive}>
      {loading ? (
        <div className={"loader"}>
          <span>Loading...</span>
        </div>
      ) : (
        <Header name={name} loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
      )}
    </div>
  );
}

export default Home;
