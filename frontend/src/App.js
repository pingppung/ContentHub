import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./routes/Home";
import NovelHome from "./routes/NovelHome";
import LoginForm from "./components/LoginForm";
import SignupForm from "./components/SignUpForm";
import MyPage from "./components/MyPage";
import Header from "./components/Header";
import Chat from "./components/Chat";
import UserService from "./services/UserService";
import AdminLayout from "./routes/AdminLayout";
import PrivateRoute from "./routes/PrivateRoute";

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [name, setName] = useState("");
  const [isChatOpen, setIsChatOpen] = useState(false); // 채팅 상태 관리

  useEffect(() => {
    // 로컬 스토리지에서 토큰을 가져오는 로직
    const token = UserService.getToken("accessToken");
    if (token != null) {
      setLoggedIn(true);
      UserService.verifyToken(token).then((res) => {
        setName(res.data.username);
      }).catch((error) => {
        setLoggedIn(false); // 토큰 검증 실패 시 로그인 상태 해제
    });
    }
  }, [name]);

// 채팅 열기/닫기 함수
  const toggleChat = () => {
    setIsChatOpen((prev) => !prev);
  };
  return (
    <Router>
        <Header onToggleChat={toggleChat} name={name} loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
        {isChatOpen && (
          <div style={{ position: 'fixed', top: '10%', right: '10%', zIndex: 1000 }}> {/* 원하는 위치 조정 */}
            <Chat isOpen={isChatOpen} onClose={toggleChat} />
          </div>
        )}
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
        <Route path="/success" element={<div>sadf</div>} />
        <Route element={<PrivateRoute redirectPath="/login"/>} >
          <Route path="/user" element={<MyPage />} /> 
        </Route>
        <Route element={<PrivateRoute redirectPath="/"/>} >
          <Route path="/admin" element={<AdminLayout name={name} />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
