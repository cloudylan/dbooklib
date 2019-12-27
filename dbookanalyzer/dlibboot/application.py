import logging as log
import dlibboot.config as config
import dlibboot.bookspider as spider
from db import mongohandler as db
log.basicConfig(level=config.LOG_LEVEL)


URL = "https://book.douban.com/subject/26853835/"
# URL = '/Users/cloudy/Downloads/test/test_string.htm'


def start():
    log.info("Application started...")
    result = "Processing"
    try:
        result = spider.get_single_book_details(URL, config.IS_TEST, db)
        result = 'Successful'
    except Exception as e:
        result = e
    finally:
        return result


start()
