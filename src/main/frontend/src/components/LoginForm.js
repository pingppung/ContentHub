import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";
import styles from "./css/LoginForm.module.css";

function LoginForm({ setLoggedIn, setUserName }) {
  const [name, setName] = useState("");
  const [pwd, setPwd] = useState("");
  const navigate = useNavigate();

  const handleLogin = (event) => {
    event.preventDefault();
    let user = {
      username: name,
      password: pwd,
    };
    UserService.login(user);
    setUserName(name);
    setLoggedIn(true);
    navigate('/');
      // .then((res) => {
      //    console.log(res.headers);
      //   // UserService.fetchToken(res.data.token);
      //   // setLoggedIn(true);
      //   // setUserName(name);
      //   navigate('/');
      // })
      // .catch((error) => {
      //   console.log(error);
      // });
      
  };

  return (
    <div className={styles.container}>
      <form id={styles.form} onSubmit={handleLogin}>
        <h1 className={styles.loginText}>로그인</h1>
        <div>
          <div className={styles.id_field}>
            <input
              required
              label="아이디"
              name="username"
              autoComplete="username"
              onChange={(e) => setName(e.target.value)}
            />
          </div>

          <div className={styles.password_field}>
            <input
              required
              label="비밀번호"
              type="password"
              name="password"
              autoComplete="current-password"
              onChange={(e) => setPwd(e.target.value)}
            />
          </div>
          <button className={styles.loginBtn} onClick={handleLogin}>
            로그인
          </button>
          <div className={styles.signupBtn}>
            아직 계정이 없으신가요?
            <a href="/signup">회원 가입</a>
          </div>
        </div>
      </form>
    </div>
  );
}

export default LoginForm;
