import axios from "axios";

class UserActivityService {

    //detail 페이지에서 나갈 때 한번만 보내기 -> 잘못누를경우 api 호출 자제하기 위함
    async addLike(title, category, token) {
        try {
            await axios.post('/user/activity/likes',
                { title, category },
                {
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`,
                    }
                }
            );
        } catch (error) {
            console.error('Error adding like:', error);
        }
    }

    async removeLike(title, category, token) {
        try {
            const response = await axios.delete(`/user/activity/likes`, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                data: { title, category }
            });
            return response.data;
        } catch (error) {
            console.error('Error removing like:', error);
            throw error;
        }
    }
    async fetchLikeStatus(title, category, token) {
        try {
            const response = await axios.get(`/user/activity/like/status`, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
                params: { title, category }
            });
            console.log(response);
            return response.data; // { liked: true/false }
        } catch (error) {
            console.error('Error checking like status:', error);
            throw error;
        }
    };

}

export default new UserActivityService();