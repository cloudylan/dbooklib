import unittest
import logging
import dlibboot.bookbee as bee
import dlibboot.config as config
import db.mongohandler as db
import io
import re

# URL = '/Users/cloudy/Downloads/test/test_string.htm'
URL = "https://book.douban.com/subject/26740503/"
config.LOG_LEVEL = logging.DEBUG


class TestBookSpider(unittest.TestCase):

    def test_getBookDetails(self):
        history_url = 'https://book.douban.com/subject/11229072/'
        result = bee.get_single_book_details(history_url, True)
        # logging.debug(result)
        # self.assertEquals(result, 'Y')

    def ttest_fetch_img(self):
        image_url = 'https://img9.doubanio.com/view/subject/l/public/s33439086.jpg'
        bee.fetch_img(image_url)

    def ttest_selenium_spider(self):
        link = "https://book.douban.com/subject/11229072/"
        comments_url = 'https://book.douban.com/subject/11229072/comments/'
        link2 = 'https://book.douban.com/subject/26853835/'
        bee.get_book_details_with_selenium(link)

    def test_get_reading_comments(self):
        link = 'https://book.douban.com/subject/26853835/'
        link2 = 'https://book.douban.com/subject/3069421/'
        response = bee.get_book_comments(link2)
        logging.debug(response)

    def test_regex(self):
        pattern = re.compile(r'http\S+/\d+/')
        test_txt = 'https://book.douban.com/subject/3069421/'
        result = re.search(pattern, test_txt)
        logging.debug(result)


if __name__ == '__main__':
    unittest.main()
