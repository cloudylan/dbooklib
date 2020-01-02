import logging as log
import sys
import dlibboot.config as config
import dlibboot.bookbee as bee
from db import mongohandler as db
log.basicConfig(level=config.LOG_LEVEL)

URL = "https://book.douban.com/subject/26853835/"


def run(read_id, link):
    log.info("Application started...")
    result = "Processing.."
    try:
        book_dic = bee.get_single_book_details(link, config.IS_TEST)
        inserted = db.save_book_detail(book_dic)
        db.save_read_info_refer_id(read_id, inserted)
        result = 'Successful'
    except Exception as e:
        result = e
    finally:
        return result


def start():
    run(sys.argv[1], sys.argv[2])
    print("Loading Successfully..." + sys.argv[2])


start()
print("Book Bee is at rest...")