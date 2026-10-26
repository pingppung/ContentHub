API_SETTINGS = {
    "movie": {
        "source": "api",

        "list_request": {
            "method": "GET",
            "url": "https://apis.wavve.com/v1/catalog",

            "params": {
                "limit": 100,
                "device": "pc",
                "drm": "wm",
                "partner": "pooq",
                "pooqzone": "none",
                "region": "kor",
                "targetage": "all",
                "apikey": "E5F3E0D30947AA5440556471321BB6D9",

                "broadcastid": "EN20309",
                "catalogType": "manualband",
                "code": "EN20309--svod--GN59",
                "manualbandId": "20309",
                "mtype": "svod",
                "offset": 0,
                "orderby": "default",
                "uicode": "EN20309",
                "uiparent": "GN59-EN20309",
                "uirank": 0,
                "uitype": "band_14",
            }
        },

        "list_paths": [
            "data.context_list"
        ],

        "id_path": "context_id",

        "detail_request": {
            "method": "GET",
            "url": "https://apis.wavve.com/fz/movie/contents/{id}"
        },

        "fields": {
            "title": {"source": "list", "path": "title", "required": True},
            "synopsis": {"source": "list", "path": "synopsis"},
            "genre": {"source": "list", "path": "genre.list[].text", "type": "list"},
            "coverImg": {"source": "list", "path": "image"},
            "source_release_date": {"source": "detail", "path": "releasedate"},
            "actors": {"source": "detail", "path": "actors.list[].text", "type": "list"},
            "countries": {"source": "detail", "path": "country", "type": "list"},
        },

        "pagination": {
            "type": "offset",
            "param": "offset",
            "step": 100
        }
    },

    "drama": {
        "source": "api",

        "list_request": {
            "method": "GET",
            "url": "https://apis.wavve.com/v1/catalog",

            "params": {
                "limit": 100,
                "device": "pc",
                "drm": "wm",
                "partner": "pooq",
                "pooqzone": "none",
                "region": "kor",
                "targetage": "all",
                "apikey": "E5F3E0D30947AA5440556471321BB6D9",

                "broadcastid": "VN1000",
                "catalogType": "generalband",
                "category": "vod",
                "genre": "drama",
                "mainCode": "GN56",
                "offset": 0,
                "orderby": "viewtime",
                "subCode": "VN1000",
                "uicode": "VN1000",
                "uiparent": "GN56-VN1000",
                "uirank": 0,
                "uitype": "band_14",
            }
        },

        "list_paths": [
            "data.context_list"
        ],

        "id_path": "context_id",

        "detail_request": {
            "method": "GET",
            "url": "https://apis.wavve.com/fz/vod/contents-detail/{id}"
        },

        "fields": {
            "title": {"source": "list", "path": "programtitle", "required": True},
            "synopsis": {"source": "list", "path": "seasonsynopsis"},
            "genre": {"source": "list", "path": "tags.list[].text", "type": "list"},
            "coverImg": {"source": "list", "path": "seasonposterimage"},
            "release_year": {"source": "detail", "path": "releaseyear"},
            "source_release_date": {"source": "detail", "path": "releasedate"},
            "actors": {"source": "detail", "path": "actors.list[].text", "type": "list"},
            "broadcasters": {"source": "detail", "path": "channelname", "type": "list"},
        },

        "pagination": {
            "type": "offset",
            "param": "offset",
            "step": 100
        }
    }
}