from flask import Flask, request, jsonify
from api_collector import api_content_handler
from crawl_collector.crawler_content_handler import CrawlerContentHandler

from utils.platform_method import PLATFORM_METHOD_BY_CATEGORY
from utils.platform_group_util import PlatformGroupUtil

app = Flask(__name__)


@app.route("/datahub/gather/content", methods=["POST"])
def gather_content():
    data = request.get_json()
    platform = data.get("platform")
    category = data.get("category")
    print(platform, category)
    platform_group = PlatformGroupUtil.get_platform_group(platform).value

    print(platform_group)
    # //display = PlatformType.from_display_name(platform_group)
    method = PLATFORM_METHOD_BY_CATEGORY[platform_group][category]
    print(method)
    # if display.method == PlatformMethod.API:
    #     results = api_content_handler.collect_content(platform_group, category)
    # else:
    #     handler = CrawlerContentHandler()
    #     try:
    #         results = handler.collect_content(platform_group, category)
    #     finally:
    #         handler.close()  # WebDriver 닫기

    return jsonify({"status": "success", "data": "a"})


# @app.route("/crawl", methods=["GET"])
# def crawl():
#     # platform = request.args.get("platform")  # 플랫폼 (naver, kakao 등)
#     # category = request.args.get("category")  # 카테고리 (novel, webtoon 등)
#     try:
#         driver = WebDriverManager.get_driver()

#         results = {
#             "novel": {},
#             "webtoon": {},
#         }

#         naver_crawler = NaverSeriesCrawler(driver)
#         naver_data = naver_crawler.crawl()
#         results["novel"]["네이버시리즈"] = naver_data

#         kakao_crawler = KakaoPageCrawler(driver)
#         kakao_data = kakao_crawler.crawl()
#         results["novel"]["카카오페이지"] = kakao_data

#         return jsonify({"status": "success", "data": results})
#     except Exception as e:
#         return jsonify({"status": "failure", "error": str(e)})
#     finally:
#         WebDriverManager.close_driver()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
