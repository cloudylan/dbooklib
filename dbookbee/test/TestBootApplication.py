import unittest
import dlibboot.application as app
import logging
import dlibboot.config as config

config.LOG_LEVEL = logging.DEBUG


class TestBootApplication(unittest.TestCase):

    def test_run(self):
        read_id = "5e004e2da7f1fd799d0217f8"
        link = "https://book.douban.com/subject/26740503/"
        app.run(read_id, link)


if __name__ == '__main__':
    unittest.main()
