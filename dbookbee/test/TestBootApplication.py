import unittest
import dlibboot.application as app
import logging
import dlibboot.config as config

config.LOG_LEVEL = logging.DEBUG


class TestBootApplication(unittest.TestCase):

    def test_run(self):
        read_id = "5df0a6093d1f97067898ef75"
        link = "https://book.douban.com/subject/11229072/"
        app.run(read_id, link)


if __name__ == '__main__':
    unittest.main()
