import axios from "axios";

class UserService {
  fetchToken(token) {
    localStorage.setItem("accessToken", token);
  }
  getToken() {
    return localStorage.getItem("accessToken");
  }
  removeToken() {
    localStorage.removeItem("accessToken");
  }
  verifyToken(token) {
    return axios.get("/auth/verifyToken", {
      headers: {
        "content-type": "application/json",
        Authorization: "Bearer " + token,
      },
      withCredentials: true,
    });
  }
  login(user) {
    return axios.post("/auth/login", JSON.stringify(user), {
      headers: { "Content-Type": `application/json` },
    });
  }
  signUp(user) {
    return axios.post("/auth/signup", JSON.stringify(user), {
      headers: { "Content-Type": `application/json` },
    });
  }

  gotoMyPage() {
    return axios.get("/authenticate");
  }
}

export default new UserService();
