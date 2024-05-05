import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./routes/Home";
import NovelHome from "./routes/NovelHome";
import LoginForm from "./components/LoginForm";
import SignupForm from "./components/SignUpForm";
import UserService from "./services/UserService";
function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [name, setName] = useState("");

  useEffect(() => {
    // 로컬 스토리지에서 토큰을 가져오는 로직
    const token = UserService.getToken("accessToken");
    if (token) {
      setLoggedIn(true);
      UserService.verifyToken(token).then((res) => {
        setName(res.data);
      });
    }
  }, [name]);

  return (
    <Router>
      <Routes>
        <Route path="/novel" element={<NovelHome />} />
        <Route path="/novel/:genre" element={<NovelHome />} />
        <Route path="/novel/search/:title" element={<NovelHome />} />
        <Route
          path="/login"
          element={
            <LoginForm setLoggedIn={setLoggedIn} setUserName={setName} />
          }
        />
        <Route path="/signup" element={<SignupForm />} />
        <Route
          path="/"
          element={
            <Home name={name} loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
