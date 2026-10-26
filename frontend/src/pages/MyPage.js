import React, { useState, useEffect } from "react";
import AuthService from "../services/AuthService";

function MyPage() {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(false);
  useEffect(() => {
    let active = true;
    AuthService.getCurrentUser()
      .then(user => { if (active) setUserInfo(user); })
      .catch(() => { if (active) setError(true); });
    return () => { active = false; };
  }, []);

  if (error) return <div role="alert">회원 정보를 불러오지 못했습니다.</div>;
  if (!userInfo) return <div>로딩 중...</div>;
  return <div className="MyPage_container">{userInfo.userId}님의 페이지입니다.</div>;
}

export default MyPage;
