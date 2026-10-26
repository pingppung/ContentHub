from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import os


def activate_bot(driver):
    try:
        wait = WebDriverWait(driver, 10)

        kakao_id = os.getenv("KAKAO_ID")
        kakao_pwd = os.getenv("KAKAO_PWD")

        if not kakao_id or not kakao_pwd:
            raise ValueError("로그인 환경 변수가 없습니다.")

        login_link = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "pr-16pxr")))
        login_link.click()

        id_field = wait.until(EC.presence_of_element_located((By.ID, "loginId--1")))
        id_field.send_keys(kakao_id)
        time.sleep(1)

        pwd_field = wait.until(EC.presence_of_element_located((By.ID, "password--2")))
        pwd_field.send_keys(kakao_pwd)
        time.sleep(1)

        login_button = wait.until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, ".btn_g.highlight.submit"))
        )
        login_button.click()
        print("로그인 버튼 클릭 완료. 직접 인증 후 계속 진행됨.")



        continue_button = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "btn_agree"))
        )
        continue_button.click()

        wait.until(EC.url_contains("/menu"))

    except Exception as e:
        raise RuntimeError("로그인 실패") from e
