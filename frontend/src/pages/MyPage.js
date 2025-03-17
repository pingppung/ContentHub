import React, { useState, useEffect } from "react";
import AuthService from "../services/AuthService"; 

function MyPage() {
  const [userInfo, setUserInfo] = useState(null);
  useEffect(() => {
    try {
      const decoded = AuthService.decodeToken(); 
      console.log(decoded);
      if (decoded) {
        setUserInfo(decoded);
      } else {
        setUserInfo(null);
      }
    } catch (error) {
      console.error("디코딩 오류", error);
    }
  }, []);

  if (!userInfo) {
    return <div>로딩 중...</div>;  // userInfo가 null일 경우 로딩 중 표시
  }
  
  return (
    <div className="MyPage_container">{userInfo.username}님의 페이지입니다.</div>
  );
}
export default MyPage;
