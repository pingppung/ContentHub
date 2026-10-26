import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import AuthService from "../../services/AuthService";
import logo from "../../images/logo.PNG";
// import chatIcon from '../../images/chatIcon.png';
import styles from "../css/Header.module.css";

function Header({ onSearch, variant }) {
  const [userInfo, setUserInfo] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [, setSelectedMenu] = useState('home');
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    let active = true;
    const token = AuthService.getToken();
    setUserInfo(null);
    setIsLoggedIn(false);
    if (token) {
      AuthService.getCurrentUser(token).then(user => {
        if (!active) return;
        setUserInfo(user);
        setIsLoggedIn(true);
      }).catch(error => {
        if (active && error.response?.status === 401) AuthService.removeToken();
      });
    }
    return () => { active = false; };
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
  };

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
  };

  return (
    <div className={`${styles.header_container} ${variant === 'discovery' ? styles.discovery : ''}`}>
      <div className={styles.headerInner}>
        <img src={logo} alt="Logo" />
        <nav>
          <ul>
            <li
              className={location.pathname === '/' ? styles.select : ''}
              onClick={() => {
                handleMenuClick('home');
                navigate('/');
              }}>
              <Link to="/">HOME</Link>
            </li>
            <li
              className={location.pathname === '/contents/novel' ? styles.select : ''}
              onClick={() => {
                handleMenuClick('novel');
                navigate('/contents/novel');
              }}>
              <Link to="/contents/novel">
                소설
              </Link>
            </li>
            <li
              className={location.pathname === '/contents/webtoon' ? styles.select : ''}
              onClick={() => {
                handleMenuClick('webtoon');
                navigate('/contents/webtoon');
              }}>
              <Link to="/contents/webtoon">
                웹툰
              </Link>
            </li>
            <li
              className={location.pathname === '/contents/drama' ? styles.select : ''}
              onClick={() => {
                handleMenuClick('drama');
                navigate('/contents/drama');
              }}>
              <Link to="/contents/drama">
                드라마
              </Link>
            </li>
            <li
              className={location.pathname === '/contents/movie' ? styles.select : ''}
              onClick={() => {
                handleMenuClick('movie');
                navigate('/contents/movie');
              }}>
              <Link to="/contents/movie">
                영화
              </Link>
            </li>
          </ul>
        </nav>
        <div className={styles.spacer}></div>
        <div onClick={goToMyPage}>마이페이지</div>
        {isLoggedIn ? (
          <>
            {!userInfo ? (
              <div>로딩 중...</div>
            ) : (
              <div className={styles.userName}>{userInfo.userId}님</div>
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
