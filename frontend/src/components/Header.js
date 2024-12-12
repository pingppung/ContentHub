import React, { useState, useEffect } from "react";
import logo from "./images/logo.PNG";
import styles from "./css/Header.module.css";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";
import Chat from "./Chat.js";
import chatIcon from './images/chatIcon.png';

function Header({ onToggleChat, name, loggedIn, setLoggedIn }) {

  const [isChatOpen, setIsChatOpen] = useState(false);
  const navigate = useNavigate();

  const handleLoginClick = (e) => {
    navigate("/login");
  };

  const handleLogoutClick = (e) => {
    setLoggedIn(false);
    UserService.removeToken();
    navigate("/");
  };

  const goToMyPage = (e) => {
    navigate("/user");
//     const token = UserService.getToken("accessToken");
//     UserService.verifyToken(token).then((res) => {
//       navigate("/user");
//     }).catch((error) => {
//       console.log(error);
//       navigate("/login");
//       //window.alert("로그인 후에 이용해줏");
//    });
  };


    return (
        <div className={styles.header_container}>
          <div className={styles.headerInner}> {/* 새로 추가된 감싸는 div */}
            <img src={logo} alt="Logo" />
            <nav>
              <ul>
                <li className={styles.select}>
                  <a href="/">HOME</a>
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
              <>
                <div className={styles.userName}>{name}님</div>
                <button className={styles.logout} onClick={handleLogoutClick}>
                  로그아웃
                </button>
              </>
            ) : (
              <button className={styles.login} onClick={handleLoginClick}>
                로그인
              </button>
            )}
            <img src={chatIcon} alt="Chat Icon" onClick={onToggleChat} className={styles.chatIcon} />
          </div>
        </div>
      );
    }
export default Header;
