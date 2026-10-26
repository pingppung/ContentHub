  import axios from "axios";



class AuthService {

  getToken() {
    return localStorage.getItem("accessToken");
  }

  removeToken() {
    localStorage.removeItem("accessToken");
  }

  async getCurrentUser(token = this.getToken()) {
    const response = await axios.get("/auth/me", {
      headers: { Authorization: "Bearer " + token },
      withCredentials: true,
    });
    return response.data;
  }

  async signUp(user) {
    try {
      console.log(user);
      const response = await axios.post("/auth/signup", user,
        { headers: { "Content-Type": "application/json" } });
      console.log("회원가입 성공:", response.data);
      return response.data; 
    } catch (error) {
      console.error("회원가입 오류:", error.response?.data || error.message);
      throw error;
    }
  }
  async login(user) {
    try {
      const response = await axios.post("/auth/login", user, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true, // 쿠키를 포함하여 서버와 통신
      });
      const authHeader = response.headers.authorization;
      if (!authHeader) throw new Error("Authorization header not found.");

      // 토큰 추출 및 반환
      const token = authHeader.startsWith("Bearer ")
        ? authHeader.substring(7)
        : authHeader;

      // 로컬 스토리지에 토큰 저장
      localStorage.setItem("accessToken", token);
      console.log("로그인 성공:", response);
      return response; // 응답 데이터 반환
    } catch (error) {
      window.alert("아이디나 비밀번호 중 틀렸습니다")
      throw error; // 에러를 호출한 곳에서 처리하도록 전달
    }
  }


}

const service = new AuthService();
export default service;
