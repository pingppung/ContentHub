SELECTORS = {
    "novel": {
        "list_url": "https://page.kakao.com/menu/10011/screen/94",
        "detail_url": "https://page.kakao.com/content/{id}?tab_type=about",
        "pagination": {
            "type": "scroll",
        },
        "list_selector": (
            "//*[@id='__next']/div/div[2]/div/div[2]/div[3]"
            "/div/div[2]/div/div/div/div"
        ),
        "wait_xpath": "//*[@id='main-content']/div[1]/div/div[1]/div[1]/div/div[2]/div[1]/span[1]",
        "fields": {
            "title": {
                "xpath": "//*[@id='main-content']/div[1]/div/div[1]/div[1]/div/div[2]/div[1]/span[1]",
                "type": "text",
                "required": True,
                "transform": {"remove": r"\[.*?\]"},
            },
            "coverImg": {
                "xpath": "//*[@id='main-content']//img[@alt='썸네일']",
                "type": "attribute",
                "attribute": "src",
            },
            "synopsis": {
                "xpath": '//*[@id="main-content"]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div/div/span',
                "type": "text",
            },
            "genre": {
                "xpath": '//*[@id="main-content"]/div[1]/div/div[1]/div[1]/div/div[2]/div[1]/div[1]/div[1]/div/span[2]',
                "type": "list",
                "transform": {"split": r"[·/,|]"},
            },
            "authors": {
                "xpath": '//*[@id="main-content"]/div[1]/div/div[2]/div[2]/div/div/div[4]/div[2]/div/div[1]/span[2]',
                "type": "list",
            },
            "age_rating": {
                "xpath": '//*[@id="main-content"]/div[1]/div/div[2]/div[2]/div/div/div[4]/div[2]/div/div[4]/span[2]',
                "type": "text",
            },
            "release_days": {
                "xpath": '//*[@id="main-content"]/div[1]/div/div[1]/div[1]/div/div[2]/div[1]/div[2]/span',
                "type": "list",
            },
        },
        "id": {
            "source": "url",
            "regex": r"/content/(\d+)",
        },
    },
}
