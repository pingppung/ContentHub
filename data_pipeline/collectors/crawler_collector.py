import re
from time import sleep
from urllib.parse import parse_qs, urljoin, urlparse
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from data_pipeline.collectors.base import ContentCollector
from data_pipeline.infrastructure.webdriver_manager import WebDriverManager

TEST_LIMIT = 2

class CrawlerCollector(ContentCollector):

    def __init__(self, settings: dict):
        self.settings = settings
        self.driver = WebDriverManager.get_driver()

        self.seen_ids = set()

        self.pagination_handlers = {
            "page": self._collect_page,
            "scroll": self._collect_scroll,
        }

    def collect(self):
        pagination_type = self.settings["pagination"]["type"]

        handler = self.pagination_handlers.get(pagination_type)

        if handler is None:
            raise ValueError(
                f"지원하지 않는 방식입니다: {pagination_type}"
            )

        login = self.settings.get("login")
        if login and login.get("enabled"):
            self.driver.get(self.settings["list_url"].format(page=1))
            login["handler"](self.driver)

        return handler()

    def _wait_for_items(self):
        try:
            return WebDriverWait(self.driver, 10).until(
                EC.presence_of_all_elements_located(
                    (By.XPATH, self.settings["list_selector"])
                )
            )
        except TimeoutException as error:
            empty_xpath = self.settings.get("empty_xpath")
            if empty_xpath and self.driver.find_elements(By.XPATH, empty_xpath):
                return []
            raise RuntimeError("목록 로딩 실패 또는 목록 선택자 오류") from error

    def _collect_items(self, remaining=TEST_LIMIT):
        items = self._wait_for_items()
        ids = []
        for item in items:
            item_id = self._extract_id(item)
            if not item_id:
                raise ValueError("목록 항목에서 콘텐츠 ID를 찾지 못했습니다.")
            if item_id not in self.seen_ids:
                self.seen_ids.add(item_id)
                ids.append(item_id)

        if items and not ids:
            raise RuntimeError("목록 페이지가 반복되었습니다.")

        results = []
        for item_id in ids[:remaining]:
            sleep(1)
            detail = self._collect_detail(self.settings["detail_url"].format(id=item_id))
            if detail is None:
                raise RuntimeError(f"상세 수집 실패: {item_id}")
            detail["id"] = item_id
            results.append(detail)
        return results

    def _next_page_url(self, page):
        pagination = self.settings["pagination"]
        container = self.driver.find_element(By.XPATH, pagination["pageCount"])
        for link in container.find_elements(By.TAG_NAME, "a"):
            href = link.get_attribute("href")
            if not href:
                continue
            url = urljoin(self.driver.current_url, href)
            values = parse_qs(urlparse(url).query).get("page", [])
            if values == [str(page + 1)]:
                return url
        return None

    def _collect_page(self):
        page = self.settings["pagination"].get("start", 1)
        url = self.settings["list_url"].format(page=page)
        results = []
        max_pages = self.settings["pagination"].get("max_pages", 100)

        for _ in range(max_pages):
            self.driver.get(url)
            if not self._wait_for_items():
                return results
            next_url = self._next_page_url(page)
            results.extend(self._collect_items(TEST_LIMIT - len(results)))
            if len(results) >= TEST_LIMIT or not next_url:
                return results
            url = next_url
            page += 1

        raise RuntimeError("최대 페이지 수를 초과했습니다.")

    def _collect_scroll(self):
        self.driver.get(self.settings["list_url"])
        if not self._wait_for_items():
            return []

        unchanged = 0
        max_scrolls = self.settings["pagination"].get("max_scrolls", 100)
        for _ in range(max_scrolls):
            count = len(self.driver.find_elements(By.XPATH, self.settings["list_selector"]))
            height = self.driver.execute_script("return document.body.scrollHeight")
            self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight)")
            try:
                WebDriverWait(self.driver, 3).until(
                    lambda driver: len(driver.find_elements(
                        By.XPATH, self.settings["list_selector"]
                    )) > count or driver.execute_script(
                        "return document.body.scrollHeight"
                    ) > height
                )
                unchanged = 0
            except TimeoutException:
                unchanged += 1
                if unchanged >= 3:
                    return self._collect_items()
        raise RuntimeError("최대 스크롤 횟수를 초과했습니다.")

    def _collect_detail(self, url: str):
        self.driver.get(url)

        wait_xpath = self.settings.get("wait_xpath")
        if wait_xpath:
            try:
                WebDriverWait(self.driver, 5).until(
                    EC.presence_of_element_located((By.XPATH, wait_xpath))
                )
            except TimeoutException:
                print(f"[DETAIL TIMEOUT] {url}")
                return None

        detail = {}

        for name, field in self.settings["fields"].items():
            value = self._read_field(field)
            detail[name] = self._transform_value(value, field)

            if field.get("required") and (
                detail[name] is None or detail[name] == "" or detail[name] == []
            ):
                print(f"[DETAIL SKIP] {url} missing={name}")
                return None

        return detail

    def _read_field(self, field: dict):
        field_type = field["type"]
        xpath = field.get("xpath")
        if not field.get("enabled", True) or not xpath or xpath == "추가":
            if field.get("required"):
                raise ValueError("필수 필드의 XPath가 설정되지 않았습니다.")
            return None

        if field_type == "text":
            return self._get_first_text(xpath)

        if field_type == "list":
            return self._get_text_list(xpath)

        if field_type == "attribute":
            return self._get_attribute(xpath, field["attribute"])

        if field_type == "exists":
            return self._exists(xpath)

        raise ValueError(f"지원하지 않는 읽기 방식입니다: {field_type}")

    def _transform_value(self, value, field: dict):
        if value is None:
            return None

        transform = field.get("transform", {})

        if field["type"] == "exists" and "true" in transform and "false" in transform:
            return transform["true"] if value else transform["false"]

        if not transform:
            return value

        values = value if isinstance(value, list) else [value]
        results = []

        for item in values:
            if not isinstance(item, str):
                results.append(item)
                continue

            if transform.get("remove"):
                item = re.sub(transform["remove"], "", item)

            if transform.get("clean"):
                item = re.sub(r"\s+", " ", item)

            if transform.get("split"):
                results.extend(
                    part.strip()
                    for part in re.split(transform["split"], item)
                    if part.strip()
                )
            else:
                results.append(item.strip())

        if isinstance(value, list) or transform.get("split"):
            return results

        return results[0]

    def _extract_id(self, element):
        id_settings = self.settings["id"]

        source = id_settings.get("source")
        pattern = id_settings.get("regex")

        if source == "url":
            link = element.find_element(
                By.TAG_NAME,
                "a"
            )

            href = link.get_attribute("href")

            match = re.search(pattern, href)

            if match:
                return match.group(1)

        return None

    def _get_text(self, xpath):
        if not xpath:
            return None

        try:
            return self.driver.find_element(
                By.XPATH,
                xpath
            ).text.strip()

        except NoSuchElementException:
            return None

    def _get_first_text(self, xpaths):
        if not xpaths:
            return None

        if isinstance(xpaths, str):
            xpaths = [xpaths]

        for xpath in xpaths:
            value = self._get_text(xpath)

            if value:
                return value

        return None

    def _get_text_list(self, xpath):
        if not xpath:
            return []

        elements = self.driver.find_elements(
            By.XPATH,
            xpath
        )

        return [
            element.text.strip()
            for element in elements
            if element.text.strip()
        ]

    def _get_attribute(
        self,
        xpath,
        attribute
    ):
        if not xpath:
            return None

        try:
            element = self.driver.find_element(
                By.XPATH,
                xpath
            )

            return element.get_attribute(attribute)

        except NoSuchElementException:
            return None

    def _exists(self, xpath):
        if not xpath:
            return False

        return bool(
            self.driver.find_elements(By.XPATH, xpath)
        )

    def close(self):
        self.driver.quit()
