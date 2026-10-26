from typing import Literal

from pydantic import BaseModel


class CollectionRequest(BaseModel):
    platform: Literal["KAKAO_PAGE", "NAVER_SERIES", "NAVER_WEBTOON", "WAVVE"]
    type: Literal["NOVEL", "WEBTOON", "MOVIE", "DRAMA"]
