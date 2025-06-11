from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from time import sleep
import logging
import re
from .kako_login import activate_bot
from ..xpaths.kakao_novel_xpath import KakaoNovelXPath


class KakaoPageCrawler:
    def __init__(self, driver: webdriver.Chrome):
        self.driver = driver
        self.driver.get(KakaoNovelXPath.URL.value)
        activate_bot(driver)

    def crawl(self):
        novels = []
        try:
            self.scroll_page_to_bottom()

            novel_links = self.get_kakao_page_data()
            for href in novel_links:
                try:
                    content = self.extract_novel_data(href)
                    novels.append(content)
                except Exception as e:
                    logging.error(f"예상치 못한 오류 발생 - 다음 항목으로 진행: {href}")
                    # logging.exception(e)
        except TimeoutException as e:
            raise Exception("TimeoutException 발생", e)
        return novels

    def scroll_page_to_bottom(self):
        js = self.driver.execute_script
        while True:
            last_height = js("return document.body.scrollHeight")
            js("window.scrollTo(0, document.body.scrollHeight)")
            sleep(1)
            new_height = js("return document.body.scrollHeight")
            if new_height == last_height:
                break

    def get_kakao_page_data(self) -> list:
        novel_links = []

        wait = WebDriverWait(self.driver, 5)
        elements = wait.until(
            EC.presence_of_all_elements_located((By.XPATH, KakaoNovelXPath.LIST.value))
        )

        for el in elements:
            href = el.find_element(By.XPATH, ".//a").get_attribute("href")
            novel_links.append(href)

        return novel_links

    def navigate_to_page(self, url: str):
        self.driver.get(url)

        wait = WebDriverWait(self.driver, 12)
        wait.until(EC.url_to_be(url))

    def extract_novel_data(self, detail_href: str) -> dict:
        content_id = self.extract_content_id(detail_href)
        url = KakaoNovelXPath.DETAIL_URL.build_url(content_id)
        # 상세 페이지로 이동
        self.navigate_to_page(url)

        # 상세 정보 추출
        original_title = self.driver.find_element(
            By.XPATH, KakaoNovelXPath.TITLE.value
        ).text
        is_adult_content = self.contains_adult_tag(original_title)  # 성인 여부 체크
        title = self.extract_title(original_title)  # 실제 제목 정리
        description = self.driver.find_element(
            By.XPATH, KakaoNovelXPath.DESCRIPTION.value
        ).text
        cover_img = self.driver.find_element(
            By.XPATH, KakaoNovelXPath.COVER_IMG.value
        ).get_attribute("src")
        genre = self.driver.find_element(By.XPATH, KakaoNovelXPath.GENRE.value).text

        return {
            "title": title,
            "description": description,
            "cover_img": cover_img,
            "genre": genre,
            "is_adult_content": is_adult_content,
            "content_id": content_id,
        }

    def contains_adult_tag(self, title: str) -> bool:
        pattern = r"\[(.*?)\]"
        matches = re.findall(pattern, title)
        for match in matches:
            if "19세" in match:
                return True
        return False

    def extract_title(self, title: str) -> str:
        return re.sub(r"\[.*?\]", "", title).strip()

    def extract_content_id(self, detail_href: str) -> str:
        # 숫자만 추출
        match = re.search(r"/content/(\d+)", detail_href)
        if match:
            return match.group(1)
        else:
            print("해당 작품 id를 찾을 수가 없습니다!")
            return None
