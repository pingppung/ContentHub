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
