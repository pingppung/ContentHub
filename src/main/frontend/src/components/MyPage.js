import React, { useState, useEffect } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import UserService from "../services/UserService";

function MyPage() {
  const outletContext = useOutletContext(); // 부모 라우트의 컨텍스트 사용
    const { data } = outletContext; // 부모 라우트에서 전달된 data

  console.log(data);
  return (
    <div className="MyPage_container">{data}님의 페이지입니다.</div>
  );
}
export default MyPage;
