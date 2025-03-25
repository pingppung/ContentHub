from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from time import sleep
import logging
import re
from services.naver_login import activate_bot
from enums.naver_novel_xpath import NaverNovelXPath

class NaverSeriesCrawler:
    def __init__(self, driver: webdriver.Chrome):
        self.driver = driver
        self.driver.get(NaverNovelXPath.URL.value)
        activate_bot(driver)

    def crawl(self):
        novels = []
        try:
            total_pages = self.get_last_page_number()
            novel_links = self.get_naver_series_data(total_pages)
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

    def get_last_page_number(self):
        try:
            wait = WebDriverWait(self.driver, 10)
            pagination = wait.until(EC.presence_of_element_located((By.XPATH, NaverNovelXPath.PAGE_COUNT.value)))
            page_links = pagination.find_elements(By.TAG_NAME, "a")
            total_pages = len(page_links)
            return total_pages
        except Exception as e:
            print("에러: {e}")
            return 1

    def navigate_to_page(self, url: str):
        self.driver.get(url)

        wait = WebDriverWait(self.driver, 10)
        wait.until(EC.url_to_be(url))

    def get_naver_series_data(self, total_pages: int) -> list:
        novel_links = []
        for i in range(1, total_pages + 1):

            url = f"{NaverNovelXPath.URL.value}{i}"
            self.navigate_to_page(url)

            wait = WebDriverWait(self.driver, 5)

            elements = wait.until(
                lambda driver: driver.find_elements(By.XPATH, NaverNovelXPath.LIST.value)
            )

            for el in elements:
                href = el.find_element(By.XPATH, ".//a").get_attribute("href")
                novel_links.append(href)

        return novel_links

    def extract_novel_data(self, detail_href: str) -> dict:
        content_id = self.extract_content_id(detail_href)
        url = NaverNovelXPath.DETAIL_URL.build_url(content_id)

        # 상세 페이지로 이동
        self.navigate_to_page(url)

        # 상세 정보 추출
        original_title = self.driver.find_element(By.XPATH, NaverNovelXPath.TITLE.value).text
        title = self.extract_title(original_title)  # 실제 제목 -[] 제외
        is_adult_content = self.contains_adult_tag()  # 성인 여부 체크
        description = self.get_description()
        cover_img = self.driver.find_element(By.XPATH, NaverNovelXPath.COVER_IMG.value).get_attribute("src")
        genre = self.driver.find_element(By.XPATH, NaverNovelXPath.GENRE.value).text
        return {
            "title": title,
            "description": description,
            "cover_img": cover_img,
            "genre": genre,
            "is_adult_content": is_adult_content,
            "content_id": content_id,
        }

    def contains_adult_tag(self):
        return len(self.driver.find_elements(By.XPATH, NaverNovelXPath.IS_ADULT.value)) > 0

    def extract_title(self, title):
        return re.sub(r"\[.*?\]", "", title).strip()

    def extract_content_id(self, input_str):
        match = re.search(r"\d+", input_str)
        if match:
            return match.group()
        else:
            print("해당 작품 id를 찾을 수가 없습니다!")
            return None

    def get_description(self):
        try:
            element = self.driver.find_element(By.XPATH, NaverNovelXPath.DESCRIPTION_WITH_MORE.value)
            description = self.driver.execute_script("return arguments[0].innerText.trim();", element)
            return self.clean_text(description)
        except Exception:
            try:
                description = self.driver.find_element(By.XPATH, NaverNovelXPath.DESCRIPTION_NO_MORE.value).text
                return self.clean_text(description)
            except Exception:
                return "설명 없음"

    def clean_text(self, text):
        # 불필요한 특수문자 및 "접기" 텍스트를 제거
        return text.replace("\u00A0", " ").replace("\u200B", " ").replace("\u200C", " ")\
            .replace("\u200D", " ").replace("\uFEFF", " ").replace("접기", "").strip()
