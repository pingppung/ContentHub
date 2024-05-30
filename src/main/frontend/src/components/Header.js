import React, { useState, useEffect } from "react";
import logo from "./images/logo.PNG";
import styles from "./css/Header.module.css";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";

function Header({ name, loggedIn, setLoggedIn }) {

  const navigate = useNavigate();

  const handleLoginClick = (e) => {
    navigate("/login");
  };

  const handleLogoutClick = (e) => {
    setLoggedIn(false);
    UserService.removeToken();
  };
  const goToMyPage = (e) => {
    navigate("/user");
    // const token = UserService.getToken("accessToken");
    // UserService.verifyToken(token).then((res) => {
    //   navigate("/user");
    // }).catch((error) => {
    //   console.log(error);
    //   navigate("/login");
    //   //window.alert("로그인 후에 이용해줏");
    // });
  };
  //https://gaemi606.tistory.com/entry/React-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%A0%95%EB%B3%B4-%EC%97%86%EC%9D%84-%EA%B2%BD%EC%9A%B0-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%A1%9C-redirect%ED%95%98%EA%B8%B0-react-router-PrivateRoute
  return (
    <div className={styles.header_container}>
      <img src={logo} alt="Logo" />
      <nav>
        <ul>
          <li className={styles.select}>
            <a href="#">HOME</a>
          </li>
          <li>
            <a href="/novel" className={styles.novel}>
              웹소설
            </a>
          </li>
        </ul>
      </nav>
      <div style={{ flex: "1 0 0" }}></div>
      <div onClick={goToMyPage}>마이페이지</div>
      {loggedIn ? (
        // 로그인 상태일 때
        <>
          <div className={styles.userName}>{name}</div>
          <button className={styles.logout} onClick={handleLogoutClick}>
            로그아웃
          </button>
        </>
      ) : (
        // 로그아웃 상태일 때
        <button className={styles.login} onClick={handleLoginClick}>
          로그인
        </button>
      )}
    </div>
  );
}
export default Header;
