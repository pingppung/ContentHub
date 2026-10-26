API_SETTINGS = {
    "webtoon": {
        "list_request": {
            "method": "GET",
            "url": "https://comic.naver.com/api/webtoon/titlelist/weekday",
            "params": {
                "order": "user"
            }
        },

        "list_paths": [
            "titleListMap.MONDAY",
            "titleListMap.TUESDAY",
            "titleListMap.WEDNESDAY",
            "titleListMap.THURSDAY",
            "titleListMap.FRIDAY",
            "titleListMap.SATURDAY",
            "titleListMap.SUNDAY",
        ],

        "id_path": "titleId",

        "detail_request": {
            "method": "GET",
            "url": "https://comic.naver.com/api/article/list/info?titleId={id}"
        },

        "fields": {
            "title": {"source": "list", "path": "titleName", "required": True},
            "synopsis": {"source": "list", "path": "synopsis"},
            "genre": {"source": "list", "path": "curationTagList[].tagName", "type": "list"},
            "coverImg": {"source": "list", "path": "thumbnailUrl"},
            "artists": {"source": "detail", "path": "author", "type": "list"},
            "publish": {"source": "detail", "path": "publishDescription"},
            "episode_count": {"source": "detail", "path": "episodeCount"},
            "status": {
                "source": "detail",
                "path": "finish",
                "values": {"true": "COMPLETED", "false": "ONGOING"},
            },
        },
    },
}
