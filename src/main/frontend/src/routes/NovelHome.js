import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./NovelHome.module.css";
import Novel from "../components/Novel";
function App() {
    const [loading, setLoading] = useState(true);
    const [data, setDate] = useState([]);
    useEffect(() => {
        axios
        .get("/novel")
        .then((res) => {
            setDate(res.data);
            setLoading(false);
        })
        .catch((err) => console.log(err));
    }, []);

    return (
      <div className={styles.container}>
        {loading ? (
          <div className={styles.loader}>
            <span>Loading...</span>
          </div>
        ) : (
          <div className={styles.novels}>
            {data.map((item, index) => (
              <Novel
                key={index}
                coverImg={item.coverImg}
                title={item.title}
                summary={item.summary}
                genre={item.genre}
              />
            ))}
          </div>
        )}
      </div>
    );
}

export default App;
