import React, { useState, useEffect } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import UserService from "../services/UserService";
function MyPage() {
 // const outletContext = useOutletContext(); // 부모 라우트의 컨텍스트 사용
    const userInfo = useOutletContext(); // 부모 라우트에서 전달된 data

  console.log(userInfo);
  return (
    <div className="MyPage_container">{userInfo}님의 페이지입니다.</div>
  );
}
export default MyPage;
