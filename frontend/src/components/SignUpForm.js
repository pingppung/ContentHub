import React, { useState } from "react";
import styles from "./css/LoginForm.module.css";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";

function SignupForm() {
  const [name, setName] = useState("");
  const [pwd, setPwd] = useState("");
  const navigate = useNavigate();

  const handleSignUp = (event) => {
    event.preventDefault();
    let user = {
      username: name,
      password: pwd,
    };
    UserService.signUp(user)
      .then((res) => {
        navigate(`/login`);
        window.alert("회원가입에 성공했습니다");
      })
      .catch((error) => {
        // 오류 발생 시 처리
        window.alert(error.response.data);
        console.error("회원가입 오류:", error);
      });
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
          <button className={styles.signupBtn} onClick={handleSignUp}>
            회원가입
          </button>
        </div>
      </form>
    </div>
  );
}

export default SignupForm;
