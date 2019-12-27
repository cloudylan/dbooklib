import unittest
import db.mongohandler as db
import logging
import dlibboot.config as config

config.LOG_LEVEL = logging.DEBUG


class TestMongoHandler(unittest.TestCase):

    def test_save_read_info_refer_id(self):
        read_id = "5df0a6093d1f97067898ee61"
        book_id = "testid_123"
        result = db.save_read_info_refer_id(read_id, book_id)
        logging.debug(result["bookReferId"])

        self.assertNotEqual("", result["bookReferId"])


if __name__ == '__main__':
    unittest.main()
