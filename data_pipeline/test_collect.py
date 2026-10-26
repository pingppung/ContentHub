import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent

if str(PROJECT_ROOT) not in sys.path:
    sys.path.insert(0, str(PROJECT_ROOT))

from data_pipeline.enums import Platform, ContentType
from data_pipeline.services.collection_service import CollectionService


service = CollectionService()

results = service.collect(
    Platform.NAVER_SERIES,
    ContentType.NOVEL
)

print(f"수집 개수: {len(results)}")

for item in results[:5]:
    print(item)

