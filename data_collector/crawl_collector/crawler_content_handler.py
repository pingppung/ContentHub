import importlib

from .webdriver_manager import WebDriverManager
from .kakao.kakao_page_crawler import KakaoPageCrawler
from .naver.naver_series_crawler import NaverSeriesCrawler


class CrawlerContentHandler:
    def __init__(self):
        self.driver = WebDriverManager.get_driver()

    def close(self):
        WebDriverManager.close_driver()

    def load_xpath_module(self, platform_name: str):
        try:
            return importlib.import_module(
                f"crawl_collector.xpaths.{platform_name}_xpath"
            )
        except ModuleNotFoundError:
            raise ValueError(f"지원하지 않는 플랫폼입니다: {platform_name}")

    def get_xpath_class_name(self, platform_name: str, content_type: str) -> str:
        return (
            "".join(word.capitalize() for word in platform_name.split("_"))
            + content_type.capitalize()
            + "XPath"
        )

    def get_xpath_class(self, xpath_module, class_name: str):
        try:
            return getattr(xpath_module, class_name)
        except AttributeError:
            raise ValueError(f"{class_name} 클래스가 모듈에 없습니다.")

    def collect_content(self, platform_name: str, content_type: str):
        xpath_module = self.load_xpath_module(platform_name)
        class_name = self.get_xpath_class_name(platform_name, content_type)
        xpath_class = self.get_xpath_class(xpath_module, class_name)
        if platform_name == "kakao":
            crawler = KakaoPageCrawler(self.driver, xpath_class)
        elif platform_name == "naver":
            crawler = NaverSeriesCrawler(self.driver, xpath_class)
        else:
            raise ValueError("지원하지 않는 플랫폼입니다.")

        return crawler.crawl()
