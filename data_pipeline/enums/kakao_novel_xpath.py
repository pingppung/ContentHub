from enum import Enum

class KakaoNovelXPath(Enum):
    URL = "https://page.kakao.com/menu/10011/screen/94"
    DETAIL_URL = "https://page.kakao.com/content/{}?tab_type=about"
    LIST = "//*[@id='__next']/div/div[2]/div/div[2]/div[3]/div/div[2]/div/div/div/div"
    TITLE = "//*[@id='__next']/div/div[2]/div[1]/div/div[1]/div[1]/div/div[2]/a/div/span[1]"
    COVER_IMG = "//*[@id='__next']//img[@alt='썸네일']"
    DESCRIPTION = "//*[@id='__next']/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div[2]/div//span"
    GENRE = "//*[@id='__next']/div/div[2]/div[1]/div/div[1]/div[1]/div/div[2]/a/div/div[1]/div[1]/div/span[2]"
    IS_ADULT = ".//img[@alt='19세 뱃지']"


    def build_url(self, content_id):
        return self.value.format(content_id)
