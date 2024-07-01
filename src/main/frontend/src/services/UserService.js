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
    }).then(response => {
      console.log(response);
      console.log(response.data);
      return response.data;
    });
  }
  verifyAuth(token) {
    return axios.get("/auth/verifyAuth", {
      headers: {
        "content-type": "application/json",
        Authorization: "Bearer " + token,
      },
      withCredentials: true,
    });
  }
  login(user) {
    axios.post("/login", user, {
      headers: { "Content-Type": `application/json` },
      withCredentials: true,
    }).then((res) => {
      const authHeader = res.headers.authorization;
      
      console.log(res);
      const token = authHeader.startsWith('Bearer ') ? authHeader.substring(7) : authHeader;
      this.fetchToken(token);
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
