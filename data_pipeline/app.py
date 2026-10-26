import logging

import uvicorn
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from starlette.concurrency import run_in_threadpool

from data_pipeline.collectors.collection_targets import get_collection_target
from data_pipeline.converters.content_converter import normalize_collected_content
from data_pipeline.enums import ContentType, Platform
from data_pipeline.schemas.collection_request import CollectionRequest
from data_pipeline.services.collection_service import CollectionService
from data_pipeline.services.recommendation_service import recommend_contents


app = FastAPI()
logger = logging.getLogger(__name__)
collection_service = CollectionService()


@app.post("/datahub/gather/content")
def gather_content(request: CollectionRequest):
    platform = Platform(request.platform)
    content_type = ContentType(request.type.lower())
    try:
        get_collection_target(platform, content_type)
    except ValueError as e:
        return JSONResponse(status_code=400, content={"message": str(e)})
    try:
        results = collection_service.collect(platform, content_type)

        return {
            "status": "success",
            "data": [normalize_collected_content(item, request.type) for item in results],
        }

    except Exception:
        logger.exception("Collection failed: platform=%s type=%s", request.platform, request.type)
        return JSONResponse(
            status_code=502,
            content={"message": "수집 또는 수집 결과 변환에 실패했습니다."}
        )


@app.post("/recommend/similar")
async def recommend(request: Request):
    data = await request.json()
    if not data.get("query"):
        return JSONResponse(
            status_code=400,
            content={"message": "기준 콘텐츠가 없습니다."}
        )

    return await run_in_threadpool(recommend_contents, data)


if __name__ == "__main__":
    uvicorn.run("data_pipeline.app:app", host="0.0.0.0", port=5000)
