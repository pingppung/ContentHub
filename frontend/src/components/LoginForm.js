import React, { useState } from "react";
import AuthService from "../services/AuthService";
import styles from "./css/LoginForm.module.css";

function LoginForm({ onAuthSuccess }) {
  const [userId, setId] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (event) => {
    event.preventDefault();
    try {
      const res = await AuthService.login({ userId, password });
      onAuthSuccess(userId); // 인증 성공 시 부모 컴포넌트에 전달
    } catch (error) {
      console.error("로그인 오류 : ", error);
    }
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
              autoComplete="id"
              onChange={(e) => setId(e.target.value)}
            />
          </div>

          <div className={styles.password_field}>
            <input
              required
              label="비밀번호"
              type="password"
              name="password"
              autoComplete="current-password"
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button className={styles.loginBtn} onClick={handleLogin}>
            로그인
          </button>
          <div className={styles.signupBtn}>
            아직 계정이 없으신가요?
            <a href="/auth/signup">회원 가입</a>
          </div>
        </div>
      </form>
    </div>
  );
}

export default LoginForm;
