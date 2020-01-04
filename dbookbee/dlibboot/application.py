import logging as log
import sys
import dlibboot.config as config
import dlibboot.bookbee as bee
from db import mongohandler as db
log.basicConfig(level=config.LOG_LEVEL)


def run(read_id, link):
    log.info("Application started...")
    try:
        book_dic = bee.get_single_book_details(link, config.IS_TEST)
        inserted = db.save_book_detail(book_dic)
        db.save_read_info_refer_id(read_id, inserted)
        return book_dic['title']
    except Exception as e:
        return e


def start():
    result = run(sys.argv[1], sys.argv[2])
    print(result + " is loaded Successfully...")


start()

