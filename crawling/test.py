import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import time

base_url = "https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page="

for page_number in range(1, 6):
    url = base_url + str(page_number)
    res = requests.get(url)

    if res.status_code == 200:
        soup = BeautifulSoup(res.text, 'html.parser')
        contents = soup.select('.comic_cont a.NPI\\=a\\:content')
        for content in contents:
            print(content.text.strip())

