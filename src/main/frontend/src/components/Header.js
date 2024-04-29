import React from "react";
import logo from "./images/logo.PNG";
import styles from "./css/Header.module.css";

function Header() {
  const [loginCheck, setLoginCheck] = useState(false); // 로그인 상태 체크
  const [userName, setUserName] = useState(""); // 사용자 이름 상태
  const navigate = useNavigate();

  const handleLoginClick = (e) => {
    navigate("/login");
  };

  const handleLoginSuccess = (userName) => {
    setLoginCheck(true); // 로그인 상태를 true로 업데이트
    setUserName(userName); // 사용자 이름 설정
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
      {loginCheck ? (
        // 로그인 상태일 때
        <div className={styles.userName}>{userName}</div>
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
