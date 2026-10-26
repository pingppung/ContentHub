import axios from "axios";
import AuthService from "./AuthService";

class AdminStatisticsService {
  async getContentCount() {
    console.log("sdaf");
    const token = AuthService.getToken();
    try {
      const response = await axios.get("/admin/statistics", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error("Error fetching content count:", error);
      throw error;
    }
  }
}

const service = new AdminStatisticsService();
export default service;  