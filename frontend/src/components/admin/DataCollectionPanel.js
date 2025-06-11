import React, { useState } from 'react';
import {
    Paper,
    Typography,
    Box,
    FormGroup,
    FormControlLabel,
    Checkbox,
    Button,
    Divider,
} from '@mui/material';

const DataCollectionPanel = () => {
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [selectedPlatforms, setSelectedPlatforms] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState([]);

    const categories = [
        { id: 'movie', name: '영화' },
        { id: 'drama', name: '드라마' },
        { id: 'animation', name: '애니메이션' },
        { id: 'novel', name: '소설' },
        { id: 'webtoon', name: '웹툰' },
    ];

    const platforms = {
        movie: ['넷플릭스', '웨이브', '티빙'],
        drama: ['넷플릭스', '웨이브', '티빙'],
        animation: ['넷플릭스', '웨이브'],
        novel: ['네이버 시리즈', '카카오페이지', '리디북스'],
        webtoon: ['네이버 웹툰', '카카오 웹툰', '레진코믹스'],
    };

    const genres = {
        movie: ['판타지', '로맨스', '액션', '스릴러'],
        drama: ['로맨스', '스릴러', '코미디'],
    };

    const handleCategoryChange = (categoryId) => {
        setSelectedCategories(prev =>
            prev.includes(categoryId)
                ? prev.filter(id => id !== categoryId)
                : [...prev, categoryId]
        );
    };

    const handlePlatformChange = (platform) => {
        setSelectedPlatforms(prev =>
            prev.includes(platform)
                ? prev.filter(p => p !== platform)
                : [...prev, platform]
        );
    };

    const handleGenreChange = (genre) => {
        setSelectedGenres(prev =>
            prev.includes(genre)
                ? prev.filter(g => g !== genre)
                : [...prev, genre]
        );
    };

    const handleStartCollection = () => {
        console.log('Selected Categories:', selectedCategories);
        console.log('Selected Platforms:', selectedPlatforms);
        console.log('Selected Genres:', selectedGenres);
    };

    return (
        <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
                데이터 수집 설정
            </Typography>
            <Divider sx={{ mb: 3 }} />

            {/* 카테고리 선택 */}
            <Box sx={{ mb: 3 }}>
                <Typography variant="subtitle1" gutterBottom>
                    카테고리 선택
                </Typography>
                <FormGroup>
                    {categories.map(category => (
                        <FormControlLabel
                            key={category.id}
                            control={
                                <Checkbox
                                    checked={selectedCategories.includes(category.id)}
                                    onChange={() => handleCategoryChange(category.id)}
                                />
                            }
                            label={category.name}
                        />
                    ))}
                </FormGroup>
            </Box>

            {/* 플랫폼 선택 */}
            {selectedCategories.length > 0 && (
                <Box sx={{ mb: 3 }}>
                    <Typography variant="subtitle1" gutterBottom>
                        플랫폼 선택
                    </Typography>
                    <FormGroup>
                        {selectedCategories.map(categoryId =>
                            platforms[categoryId]?.map(platform => (
                                <FormControlLabel
                                    key={platform}
                                    control={
                                        <Checkbox
                                            checked={selectedPlatforms.includes(platform)}
                                            onChange={() => handlePlatformChange(platform)}
                                        />
                                    }
                                    label={platform}
                                />
                            ))
                        )}
                    </FormGroup>
                </Box>
            )}

            {/* 장르 선택 */}
            {selectedCategories.length > 0 && (
                <Box sx={{ mb: 3 }}>
                    <Typography variant="subtitle1" gutterBottom>
                        장르 선택
                    </Typography>
                    <FormGroup>
                        {selectedCategories.map(categoryId =>
                            genres[categoryId]?.map(genre => (
                                <FormControlLabel
                                    key={genre}
                                    control={
                                        <Checkbox
                                            checked={selectedGenres.includes(genre)}
                                            onChange={() => handleGenreChange(genre)}
                                        />
                                    }
                                    label={genre}
                                />
                            ))
                        )}
                    </FormGroup>
                </Box>
            )}

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleStartCollection}
                    disabled={!selectedCategories.length || !selectedPlatforms.length}
                    size="large"
                >
                    데이터 수집 시작
                </Button>
            </Box>
        </Paper>
    );
};

export default DataCollectionPanel; 