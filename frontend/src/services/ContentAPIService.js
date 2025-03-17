import axios from "axios";

async function getContentData(category, selectedGenre) {
    try {
        const response = await axios.get("/contents", {
            params: { category: category},
            headers: { "Content-Type": "application/json" }
        });

        console.log("데이터 가져오기 성공: ", response.data);
        return response.data;
    } catch (err) {
        console.log("데이터 가져오기 실패:", err);
        return null;
    }
}

async function getContentBySearch(category, selectedGenre, inputSearch) {
    try {
        const params = {};
        if (category) params.category = category;
        if (inputSearch) params.title = inputSearch;
        if (selectedGenre) params.genre = selectedGenre;
        console.log(params);
        const response = await axios.get("/contents/search", { params });
        console.log("데이터 가져오기 성공: ", response.data);
        return response.data;
    } catch (err) {
        console.log("데이터 가져오기 실패:", err);
        return null;
    }
}


export { getContentData, getContentBySearch};