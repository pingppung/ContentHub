import axios from "axios";

function searchParams(category, selectedGenre, inputSearch) {
    const type = category?.trim().toUpperCase();
    const genre = selectedGenre?.trim();
    const title = inputSearch?.trim();
    return {
        ...(type ? { type } : {}),
        ...(genre && genre !== "전체" ? { genre } : {}),
        ...(title ? { title } : {})
    };
}

async function fetchContents(params) {
    try {
        const response = await axios.get("/contents", { params });
        return response.data;
    } catch (error) {
        console.error("Failed to fetch contents:", error);
        return null;
    }
}

async function getContentData(category, selectedGenre) {
    return fetchContents(searchParams(category, selectedGenre));
}

async function getContentBySearch(category, selectedGenre, inputSearch) {
    return fetchContents(searchParams(category, selectedGenre, inputSearch));
}

export { getContentData, getContentBySearch };
