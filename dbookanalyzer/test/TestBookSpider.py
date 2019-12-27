import unittest
import logging
import dlibboot.bookspider as spider
import dlibboot.config as config
import db.mongohandler as db

# URL = '/Users/cloudy/Downloads/test/test_string.htm'
URL = "https://book.douban.com/subject/26853835/"
config.LOG_LEVEL = logging.DEBUG


class TestBookSpider(unittest.TestCase):

    def test_getBookDetails(self):
        result = spider.get_single_book_details(URL, True, db)
        logging.debug(result)
        # self.assertEquals(result, 'Y')


if __name__ == '__main__':
    unittest.main()
