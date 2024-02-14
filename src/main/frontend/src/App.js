import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./routes/Home";
import NovelHome from "./routes/NovelHome";
function App() {
    return (
        <Router>
          <Routes>
            <Route path="/novel" element={<NovelHome />} />
            <Route path="/novel/:genre" element={<NovelHome />} />
            <Route path="/novel/search/:title" element={<NovelHome />} />
            <Route path="/" element={<Home />} />
          </Routes>
        </Router>
      );
}

export default App;
