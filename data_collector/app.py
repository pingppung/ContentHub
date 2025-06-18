from flask import Flask, request, jsonify
from api_collector import api_content_handler
from crawl_collector.crawler_content_handler import CrawlerContentHandler

from utils.platform_method import PLATFORM_METHOD_BY_CATEGORY, PlatformMethod
from utils.platform_group_util import PlatformGroupUtil

app = Flask(__name__)


@app.route("/datahub/gather/content", methods=["POST"])
def gather_content():
    data = request.get_json()
    platform = data.get("platform")
    category = data.get("category")
    print(platform, category)
    platform_group = PlatformGroupUtil.get_platform_group(platform).value
    method = PLATFORM_METHOD_BY_CATEGORY[platform_group][category]
    if method == PlatformMethod.API:
        results = api_content_handler.collect_content(platform_group, category)
    else:
        handler = CrawlerContentHandler()
        try:
            results = handler.collect_content(platform_group, category)
        finally:
            handler.close()  # WebDriver 닫기

    return jsonify({"status": "success", "data": results})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
