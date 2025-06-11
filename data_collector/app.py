from flask import Flask, request, jsonify
from crawl_collector.utils.webdriver_manager import WebDriverManager
from crawl_collector.naver.naver_series_crawler import NaverSeriesCrawler
from crawl_collector.kakao.kakao_page_crawler import KakaoPageCrawler
from api_collector.api_router import collect_content

app = Flask(__name__)


@app.route("/datahub/gather/content", methods=["POST"])
def gather_content():
    data = request.get_json()
    platform = data.get("platform")
    category = data.get("category")
    print(platform, category)
    collect_content(platform, category)

    return jsonify({"status": "success", "data": "result"})

@app.route("/crawl", methods=["GET"])
def crawl():
    # platform = request.args.get("platform")  # 플랫폼 (naver, kakao 등)
    # category = request.args.get("category")  # 카테고리 (novel, webtoon 등)
    try:
        driver = WebDriverManager.get_driver()

        results = {
            "novel": {},
            "webtoon": {},
        }

        naver_crawler = NaverSeriesCrawler(driver)
        naver_data = naver_crawler.crawl()
        results["novel"]["네이버시리즈"] = naver_data

        kakao_crawler = KakaoPageCrawler(driver)
        kakao_data = kakao_crawler.crawl()
        results["novel"]["카카오페이지"] = kakao_data

        return jsonify({"status": "success", "data": results})
    except Exception as e:
        return jsonify({"status": "failure", "error": str(e)})
    finally:
        WebDriverManager.close_driver()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
