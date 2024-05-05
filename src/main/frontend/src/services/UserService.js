import axios from "axios";

class UserService {
  fetchToken(token) {
    localStorage.setItem("accessToken", token);
    console.log(" token is " + token);
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
    });
  }
  getUserName() {
    axios.get("/authenticate", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
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

  getUserById(id) {
    return axios.get("/auth/getUser", {
      params: { user_id: id },
    });
  }
}

export default new UserService();
