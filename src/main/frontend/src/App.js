import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./routes/Home";
import NovelHome from "./routes/NovelHome";
import NovelDetail from "./components/NovelDetail";
function App() {
    return (
        <Router>
          <Routes>
            <Route path="/novel" element={<NovelHome />} />
            <Route path="/novel/:genres" element={<NovelHome />} />
            <Route path="/" element={<Home />} />
          </Routes>
        </Router>
      );
}

export default App;
