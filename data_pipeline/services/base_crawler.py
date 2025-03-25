from selenium.webdriver.common.by import By

class BaseCrawler:
    def __init__(self, driver):
        self.driver = driver

    def get_page_source(self, url):
        """공통된 페이지 로딩 메서드"""
        self.driver.get(url)
        return self.driver.page_source

    def find_element(self, selector):
        """공통된 요소 찾기 메서드"""
        return self.driver.find_element(By.CSS_SELECTOR, selector)

    def wait_for_element(self, selector, timeout=10):
        """공통된 대기 메서드"""
        from selenium.webdriver.support.ui import WebDriverWait
        from selenium.webdriver.support import expected_conditions as EC
        element = WebDriverWait(self.driver, timeout).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, selector))
        )
        return element

