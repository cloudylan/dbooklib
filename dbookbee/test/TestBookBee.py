import unittest
import logging
import dlibboot.bookbee as bee
import dlibboot.config as config
import db.mongohandler as db

# URL = '/Users/cloudy/Downloads/test/test_string.htm'
URL = "https://book.douban.com/subject/26740503/"
config.LOG_LEVEL = logging.DEBUG


class TestBookSpider(unittest.TestCase):

    def ttest_getBookDetails(self):
        result = bee.get_single_book_details(URL, True, db)
        logging.debug(result)
        # self.assertEquals(result, 'Y')

    def ttest_fetch_img(self):
        image_url = 'https://img9.doubanio.com/view/subject/l/public/s33439086.jpg'
        bee.fetch_img(image_url)

    def test_selenium_spider(self):
        link = "https://book.douban.com/subject/11229072/"
        bee.get_book_details_with_selenium(link)


if __name__ == '__main__':
    unittest.main()