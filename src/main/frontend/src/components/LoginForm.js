import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";
import styles from "./css/LoginForm.module.css";
import axios from "axios";

function LoginForm() {
  const [name, setName] = useState("");
  const [pwd, setPwd] = useState("");
  const navigate = useNavigate();

  const handleLogin = (event) => {
    event.preventDefault();
    let user = {
      userName: name,
      userPwd: pwd,
    };
    UserService.login(user)
      .then((res) => {
        console.log(res);
        //UserService.fetchToken(response.data);
        // localStorage.setItem("acessToken", JSON.stringify(response.data));
        navigate(`/`);
      })
      .catch((error) => {
        console.log("error");
        window.alert("아이디나 비밀번호가 다릅니다");
      });
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
              autoComplete="username"
              onChange={(e) => setName(e.target.value)}
            />
          </div>

          <div className={styles.password_field}>
            <input
              required
              label="비밀번호"
              type="password"
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
