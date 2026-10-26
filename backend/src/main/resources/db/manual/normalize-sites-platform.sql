-- Manual data migration; this file is not executed automatically at startup.
-- Stop the application before running it against the intended database.
-- Review the stored values first. Only the explicit aliases below are changed.
USE contenthub;

SELECT platform, COUNT(*) AS row_count
FROM sites
GROUP BY platform;

START TRANSACTION;

UPDATE sites
SET platform = CASE
    WHEN LOWER(TRIM(platform)) IN ('kakao_page', 'kakaopage', '카카오페이지')
        THEN 'KAKAO_PAGE'
    WHEN LOWER(TRIM(platform)) IN ('naver_series', 'naverseries', '네이버시리즈')
        THEN 'NAVER_SERIES'
    WHEN LOWER(TRIM(platform)) IN ('naver_webtoon', 'naverwebtoon', '네이버웹툰')
        THEN 'NAVER_WEBTOON'
    WHEN LOWER(TRIM(platform)) IN ('wavve', '웨이브')
        THEN 'WAVVE'
    ELSE platform
END
WHERE LOWER(TRIM(platform)) IN (
    'kakao_page', 'kakaopage', '카카오페이지',
    'naver_series', 'naverseries', '네이버시리즈',
    'naver_webtoon', 'naverwebtoon', '네이버웹툰',
    'wavve', '웨이브'
);

-- Restore types from the original category column; preserve that column.
UPDATE sites
SET content_type = UPPER(TRIM(category))
WHERE LOWER(TRIM(category)) IN ('novel', 'webtoon', 'movie', 'drama');

-- Activate only the five legacy rows confirmed in the supplied data snapshot.
UPDATE sites
SET enabled = b'1'
WHERE (id = 1 AND platform = 'NAVER_SERIES' AND content_type = 'NOVEL')
   OR (id = 2 AND platform = 'KAKAO_PAGE' AND content_type = 'NOVEL')
   OR (id = 3 AND platform = 'WAVVE' AND content_type = 'MOVIE')
   OR (id = 5 AND platform = 'WAVVE' AND content_type = 'DRAMA')
   OR (id = 6 AND platform = 'NAVER_WEBTOON' AND content_type = 'WEBTOON');

COMMIT;

-- This query must return no rows before restarting Hibernate schema updates.
-- Unknown platforms are preserved and require an explicit mapping decision.
SELECT id, platform
FROM sites
WHERE platform IS NULL
   OR BINARY platform NOT IN ('KAKAO_PAGE', 'NAVER_SERIES', 'NAVER_WEBTOON', 'WAVVE');

SELECT id, platform, category, content_type, enabled
FROM sites
ORDER BY id;
