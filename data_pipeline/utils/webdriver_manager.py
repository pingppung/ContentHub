from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options

class WebDriverManager:
    _main_driver = None #싱글톤 패턴

    @classmethod
    def get_driver(cls):
        if cls._main_driver is None:
            try:
                chrome_options = Options();
                # chrome_options.add_argument("--headless") #GUI 없이
                chrome_options.add_argument("--remote-allow-origins=*")
                # chrome_options.add_argument( "--disable-dev-shm-usage")  # 공유 메모리 사용 안 함 (리눅스에서 속도 개선)
                serivce = Service(ChromeDriverManager().install())
                cls._main_driver = webdriver.Chrome(service=serivce, options=chrome_options)
            except Exception as e:
                raise RuntimeError("webdriver 생성 실패") from e
        return cls._main_driver

    @classmethod
    def close_driver(cls):
        if cls._main_driver is not None:
            cls._main_driver.quit()
            cls._main_driver = None
