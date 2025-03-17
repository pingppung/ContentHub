import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AuthService from "../services/AuthService";
import LoginForm from "../components/LoginForm";
import SignupForm from "../components/SignUpForm";

function AuthPage() {
  
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const { authType } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (AuthService.getToken()) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
     }
  }, []);
  
  const handleLoginSuccess = (user) => {
    navigate("/", {
      state: { isLoggedIn : true} 
    });
  };

  const handleSignupSuccess = () => {
    window.alert("회원가입에 성공했습니다");
    navigate("/auth/login");
  };

  useEffect(() => {
    if (isLoggedIn) {
      navigate("/"); 
    }
  }, [isLoggedIn, navigate]);

  return (
    <div>
      {authType === "login" && <LoginForm onAuthSuccess={handleLoginSuccess } />}
      {authType === "signup" && <SignupForm onAuthSuccess={handleSignupSuccess}/>}
    </div>
  );
}


export default AuthPage;