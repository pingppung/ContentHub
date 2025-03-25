from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import os


def activate_bot(driver):
    try:
        # driver = self.driver
        wait = WebDriverWait(driver, 10)

        naver_id = os.getenv("NAVER_ID")
        naver_pwd = os.getenv("NAVER_PWD")
        
        # 로그인 링크 클릭
        login_link = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "gnb_btn_login"))
        )
        login_link.click()

        # 아이디 입력
        id_input = wait.until(EC.presence_of_element_located((By.ID, "id")))
        driver.execute_script(
            "arguments[0].value=arguments[1]", id_input, naver_id
        )
        time.sleep(1.5)

        # 비밀번호 입력
        pwd_input = wait.until(EC.presence_of_element_located((By.ID, "pw")))
        driver.execute_script(
            "arguments[0].value=arguments[1]", pwd_input, naver_pwd
        )
        time.sleep(1.5)

        # 로그인 버튼 클릭
        login_button = wait.until(
            EC.element_to_be_clickable((By.CLASS_NAME, "btn_login"))
        )
        login_button.click()
        time.sleep(1)

        wait.until(EC.url_contains("/novel"))

    except Exception as e:
        print("Error occurred during login process:", e)
