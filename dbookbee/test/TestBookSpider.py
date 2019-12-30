import unittest
import logging
import dlibboot.bookspider as spider
import dlibboot.config as config
import db.mongohandler as db

# URL = '/Users/cloudy/Downloads/test/test_string.htm'
URL = "https://book.douban.com/subject/26740503/"
config.LOG_LEVEL = logging.DEBUG


class TestBookSpider(unittest.TestCase):

    def test_getBookDetails(self):
        result = spider.get_single_book_details(URL, True, db)
        logging.debug(result)
        # self.assertEquals(result, 'Y')

    def ttest_fetch_img(self):
        image_url = 'https://img9.doubanio.com/view/subject/m/public/s6227164.jpg'
        spider.fetch_img(image_url)


if __name__ == '__main__':
    unittest.main()
