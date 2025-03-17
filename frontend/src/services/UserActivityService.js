import axios from "axios";

class UserActivityService {

    //detail 페이지에서 나갈 때 한번만 보내기 -> 잘못누를경우 api 호출 자제하기 위함
    async addLike(contentId, category) { 
        try {
            const response = await axios.post('/like', {
                contentId,
                category,
            });
        } catch (error) {
            console.error('Error adding like:', error);
        }
    }

    async removeLike(contentId, category) {
        try {
            const response = await axios.delete(`/like`, { data: { contentId, category } });
            return response.data;
        } catch (error) {
            console.error('Error removing like:', error);
            throw error;
        }
    }
    async checkLikeStatus(contentId, category) {
        try {
            const response = await axios.get(`/like/status`, { params: { contentId, category } });
            return response.data; // { liked: true/false }
        } catch (error) {
            console.error('Error checking like status:', error);
            throw error;
        }
    };
}

export default new UserActivityService();