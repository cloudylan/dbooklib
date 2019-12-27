import unittest
import dlibboot.application as app
import logging
import dlibboot.config as config

config.LOG_LEVEL = logging.DEBUG


class TestBootApplication(unittest.TestCase):

    def test_run(self):
        read_id = "5df0a6093d1f97067898ee61"
        link = "https://book.douban.com/subject/5257905/"
        app.run(read_id, link)


if __name__ == '__main__':
    unittest.main()
