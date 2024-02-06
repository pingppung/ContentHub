import React, { useEffect, useState } from "react";
import axios from "axios";
function Home() {
  const [loading, setLoading] = useState(true);
  const [data, setDate] = useState([]);
  useEffect(() => {
    axios
      .get("/api/series")
      .then((res) => {
        setDate(res.data);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, []);
    return (
    <>
    {loading ? (
            <div className={"loader"}>
              <span>Loading...</span>
            </div>
    ) : (
      <div className="btn">
        <a href="/novel" className="novel">
          <span className="WEBnovel">웹소설</span>
        </a>
      </div>
      )}
    </>
    );
}

export default Home;
