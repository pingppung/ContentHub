import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import AuthService from "../services/AuthService";
import logo from "./images/logo.PNG";
import chatIcon from './images/chatIcon.png';
import styles from "./css/Header.module.css";
function Header() {
  const [userInfo, setUserInfo] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [selectedMenu, setSelectedMenu] = useState('home'); // 기본값은 'home'
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const initialLoginState = location.state?.isLoggedIn || false;
    setIsLoggedIn(initialLoginState);
    console.log(initialLoginState);
    try {
      const decoded = AuthService.decodeToken();
      setIsLoggedIn(true);
      if (decoded) {
        setUserInfo(decoded);
      } else {
        setUserInfo(null);
        setIsLoggedIn(false);
      }
    } catch (error) {
      console.error("디코딩 오류", error);
    }
  }, [location.state?.isLoggedIn]);

  const handleLoginClick = (e) => {
    navigate("/auth/login");
  };

  const handleLogoutClick = (e) => {
    //dispatch(clearUser());
    AuthService.removeToken();
    setIsLoggedIn(false);
    setUserInfo(null);
    // navigate("/", {
    //   state: { isLoggedIn: false }
    // });
    navigate("/");
    window.location.reload();
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

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
  };

  return (
    <div className={styles.header_container}>
      <div className={styles.headerInner}>
        <img src={logo} alt="Logo" />
        <nav>
          <ul>
            <li
              className={selectedMenu === 'home' ? styles.select : ''}
              onClick={() => handleMenuClick('home')} >
              <Link to="/">HOME</Link>
            </li>
            <li
              className={selectedMenu === 'novel' ? styles.select : ''}
              onClick={() => handleMenuClick('novel')} >
              <Link to="/contents/novel" className={styles.novel}>
                소설
              </Link>
            </li>
            <li
              className={selectedMenu === 'webtoon' ? styles.select : ''}
              onClick={() => handleMenuClick('webtoon')} >
              <Link to="/contents/webtoon" className={styles.webtoon}>
                웹툰
              </Link>
            </li>
            <li
              className={selectedMenu === 'drama' ? styles.select : ''}
              onClick={() => handleMenuClick('drama')}>
              <Link to="/contents/drama" className={styles.drama}>
                드라마
              </Link>
            </li>
            <li
              className={selectedMenu === 'movie' ? styles.select : ''}
              onClick={() => handleMenuClick('movie')}>
              <Link to="/contents/movie" className={styles.movie}>
                영화
              </Link>
            </li>
          </ul>
        </nav>
        <div style={{ flex: "1 0 0" }}></div>
        <div onClick={goToMyPage}>마이페이지</div>
        {isLoggedIn ? (
          <>
            {!userInfo ? (
              <div>로딩 중...</div>
            ) : (
              <div className={styles.userName}>{userInfo.username}님</div>
            )}
            <button className={styles.logout} onClick={handleLogoutClick}>
              로그아웃
            </button>
          </>
        ) : (
          <button className={styles.login} onClick={handleLoginClick}>
            로그인
          </button>
        )}
        {/* <img src={chatIcon} alt="Chat Icon" onClick={onToggleChat} className={styles.chatIcon} /> */}
      </div>
    </div>
  );
}
export default Header;
// {isChatOpen && (
//   <div style={{ position: 'fixed', top: '10%', right: '10%', zIndex: 1000 }}> {/* 원하는 위치 조정 */}
//     <Chat isOpen={isChatOpen} onClose={toggleChat} />
//   </div>
// )}