import React, { useEffect, useState } from "react";
import axios from "axios";
function App() {
  const [data, setDate] = useState([]);

  useEffect(() => {
    axios
      .get("/api/series")
      .then((res) => setDate(res.data))
      .catch((err) => console.log(err));
  }, []);

  return (
      <div>
        <h1>받아온 값:</h1>
        <ul>
          {data.map((title, index) => <li key={index}>{title}</li>)}
        </ul>
      </div>
    );
}

export default App;
