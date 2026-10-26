from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import os


def activate_bot(driver):
    try:
        wait = WebDriverWait(driver, 10)

        naver_id = os.getenv("NAVER_ID")
        naver_pwd = os.getenv("NAVER_PWD")

        if not naver_id or not naver_pwd:
            raise ValueError("로그인 환경 변수가 없습니다.")

        login_link = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "gnb_btn_login"))
        )
        login_link.click()

        id_input = wait.until(EC.presence_of_element_located((By.ID, "id")))
        driver.execute_script(
            "arguments[0].value=arguments[1]", id_input, naver_id
        )
        time.sleep(1.5)

        pwd_input = wait.until(EC.presence_of_element_located((By.ID, "pw")))
        driver.execute_script(
            "arguments[0].value=arguments[1]", pwd_input, naver_pwd
        )
        time.sleep(1.5)

        login_button = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "btn_login"))
        )
        login_button.click()
        time.sleep(1)

        wait.until(EC.url_contains("/novel"))

    except Exception as e:
        raise RuntimeError("로그인 실패") from e
