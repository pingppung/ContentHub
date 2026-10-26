from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options


class WebDriverManager:

    @staticmethod
    def get_driver():
        options = Options()
        try:
            service = Service(ChromeDriverManager().install())
            driver = webdriver.Chrome(service=service, options=options)
            driver.set_page_load_timeout(30)
            return driver
        except Exception as error:
            if "driver" in locals():
                driver.quit()
            raise RuntimeError("webdriver 생성 실패") from error
