import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import Home from "./pages/HomePage";
import ContentHome from "./pages/ContentHomePage";
import ContentDetail from "./pages/ContentDetailPage";
import MyPage from "./pages/MyPage";
import AuthPage from "./pages/AuthPage";
import AdminLayout from "./pages/AdminPage";
import PrivateRoute from "./routes/PrivateRoute";



function App() {
  //   const [isChatOpen, setIsChatOpen] = useState(false); // 채팅 상태 관리
  // // 채팅 열기/닫기 함수
  //   const toggleChat = () => {
  //     setIsChatOpen((prev) => !prev);
  //   };
  const location = useLocation();
  const background = location.state?.background;
  return (
    <>
      <Routes location={background || location}>
        <Route path="/contents/:category" element={<ContentHome />} />
        <Route path="/auth/:authType" element={<AuthPage />} />
        <Route path="/success" element={<div>success</div>} />
        <Route element={<PrivateRoute redirectPath="/auth/login" />}>
          <Route path="/user" element={<MyPage />} />
        </Route>
        <Route element={<PrivateRoute redirectPath="/" />}>
          <Route path="/admin" element={<AdminLayout />} />
        </Route>
        <Route path="/" element={<Home />} />
      </Routes>
      {background && (
        <Routes>
          <Route path="/content/:category/detail/:title" element={<ContentDetail />} />
        </Routes>
      )}
    </>
  );
}

export default App;
