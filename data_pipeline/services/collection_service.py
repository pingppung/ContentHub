from data_pipeline.collectors.api_collector import ApiCollector
from data_pipeline.collectors.collection_targets import get_collection_target
from data_pipeline.collectors.crawler_collector import CrawlerCollector
from data_pipeline.enums import ContentType, Platform, CollectionMethod


class CollectionService:

    COLLECTORS = {
        CollectionMethod.API: ApiCollector,
        CollectionMethod.CRAWLING: CrawlerCollector,
    }

    def collect(self, platform: Platform, content_type: ContentType):
        target = get_collection_target(platform, content_type)
        collector_class = self.COLLECTORS[target["method"]]
        collector = collector_class(target["settings"])

        try:
            return collector.collect()
        finally:
            collector.close()
