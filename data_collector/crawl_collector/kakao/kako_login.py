from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options
import time
import os


def activate_bot(driver):
    try:
        wait = WebDriverWait(driver, 10)

        kakao_id = os.getenv("KAKAO_ID")
        kakao_pwd = os.getenv("KAKAO_PWD")

        # 로그인 링크 클릭
        login_link = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "pr-16pxr")))
        login_link.click()

        # 아이디 입력
        id_field = wait.until(EC.presence_of_element_located((By.ID, "loginId--1")))
        id_field.send_keys(kakao_id)
        time.sleep(1)

        # 비밀번호 입력
        pwd_field = wait.until(EC.presence_of_element_located((By.ID, "password--2")))
        pwd_field.send_keys(kakao_pwd)
        time.sleep(1)

        # 로그인 버튼 클릭
        login_button = wait.until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, ".btn_g.highlight.submit"))
        )
        login_button.click()
        # time.sleep(1)
        print("로그인 버튼 클릭 완료. 직접 인증 후 계속 진행됨.")

        

        # 동의 버튼 클릭
        continue_button = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "btn_agree"))
        )
        # time.sleep(1)
        continue_button.click()

        # URL에 "/menu"가 포함될 때까지 대기
        wait.until(EC.url_contains("/menu"))

    except Exception as e:
        print("Error occurred during login process:", e)
