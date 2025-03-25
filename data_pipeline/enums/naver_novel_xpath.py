from enum import Enum

class NaverNovelXPath(Enum):
    URL = "https://series.naver.com/novel/specialFreeList.series?specialFreeTypeCode=HOURLYFREE&page="
    DETAIL_URL = "https://series.naver.com/novel/detail.series?productNo={}"
    LIST = '//*[@id="content"]/div/div/ul/li'
    TITLE = '//*[@id="content"]/div[1]/h2'
    COVER_IMG = '//*[@id="container"]/div[1]//img'
    DESCRIPTION_WITH_MORE = '//*[@id="content"]/div[2]/div[2]'
    DESCRIPTION_NO_MORE = '//*[@id="content"]/div[2]/div'
    GENRE = '//*[@id="content"]/ul[1]/li/ul/li[2]/span/a'
    IS_ADULT = '//*[@id="content"]/div[1]/h2/span'
    PAGE_COUNT = '//p[@class="pagenate"]'


    def build_url(self, content_id):
        return self.value.format(content_id)
