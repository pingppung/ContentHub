SELECTORS = {
    "novel": {
        "list_url": (
            "https://series.naver.com/novel/"
            "specialFreeList.series?specialFreeTypeCode=HOURLYFREE&page={page}"
        ),
        "detail_url": (
            "https://series.naver.com/novel/"
            "detail.series?productNo={id}"
        ),
        "pagination": {
            "type": "page",
            "start": 1,
            "pageCount": '//p[@class="pagenate"]',
        },
        "list_selector": '//*[@id="content"]/div/div/ul/li',
        "wait_xpath": '//*[@id="content"]/div[1]/h2',
        "fields": {
            "title": {
                "xpath": '//*[@id="content"]/div[1]/h2',
                "type": "text",
                "required": True,
                "transform": {"remove": r"\[.*?\]"},
            },
            "coverImg": {
                "xpath": '//*[@id="container"]/div[1]//img',
                "type": "attribute",
                "attribute": "src",
            },
            "synopsis": {
                "xpath": [
                    '//*[@id="content"]/div[2]/div[2]',
                    '//*[@id="content"]/div[2]/div',
                ],
                "type": "text",
            },
            "genre": {
                "xpath": '//*[@id="content"]/ul[1]/li/ul/li[2]/span/a',
                "type": "list",
                "transform": {"split": r"[·/,|]"},
            },
            "age_rating": {
                "xpath": '//*[@id="content"]/div[1]/h2/span',
                "type": "text",
            },
            "authors": {
                "xpath": "추가",
                "type": "list",
            },
            "first_published_date": {
                "xpath": "추가",
                "type": "text",
            },
            "publisher": {
                "xpath": "추가",
                "type": "text",
            },
            "status": {
                "xpath": "추가",
                "type": "text",
            },
            "release_days": {
                "xpath": "추가",
                "type": "list",
            },
            "schedule_type": {
                "xpath": "추가",
                "type": "text",
            },
            "episode_count": {
                "xpath": "추가",
                "type": "text",
            },
            "platform_published_date": {
                "xpath": "추가",
                "type": "text",
            },
            "availability_status": {
                "xpath": "추가",
                "type": "text",
            },
            "rating": {
                "xpath": "추가",
                "type": "text",
            },
            "rating_max": {
                "xpath": "추가",
                "type": "text",
            },
            "rating_count": {
                "xpath": "추가",
                "type": "text",
            },
        },
        "id": {
            "source": "url",
            "regex": r"productNo=(\d+)",
        },
    },
}
