import React, { useState } from "react";
import styles from "./css/LoginForm.module.css";
import AuthService from "../services/AuthService";

function SignupForm({ onAuthSuccess }) {
  const [userId, setId] = useState("");
  const [password, setPassword] = useState("");

  const handleSignUp = async (event) => {
    event.preventDefault();
    try {
      await AuthService.signUp({ userId, password });
      onAuthSuccess({ userId, password }); // 회원가입 성공 처리
    } catch (error) {
      console.error("회원가입 오류 :", error);
    }
  };

  return (
    <div className={styles.container}>
      <form id={styles.form}>
        <h1 className={styles.text}>회원가입</h1>
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
              autoComplete="current-password"
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button className={styles.signupBtn} onClick={handleSignUp}>
            회원가입
          </button>
        </div>
      </form>
    </div>
  );
}

export default SignupForm;
