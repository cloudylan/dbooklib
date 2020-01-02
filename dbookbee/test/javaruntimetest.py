
from sys import argv
import sys
import os

import requests

import logging as log
import dlibboot.config as config
import urllib.request as req
import urllib.parse
from bs4 import BeautifulSoup
import io
import dlibboot.config as config
import logging as log
import json
import dlibboot.bookbee as spider
log.basicConfig(level=config.LOG_LEVEL)
# URL = '/Users/cloudy/Downloads/test/test_string.htm'
URL = 'https://book.douban.com/subject/26853835/'
HTML_PARSER = 'html.parser'
user_agent = 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36'
BOOK_NUMBER_PER_PAGE = 20
RETRY = 3
log.basicConfig(level=config.LOG_LEVEL)
header = {'User-agent': user_agent,
                         'Accept':
                          'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
                         # ('Cookie',
                         #  cookie),
                         # 'Upgrade-Insecure-Requests': '1',
                         # 'Cache-Control': 'no-cache',
                         # 'Connection': 'keep-alive',
                         # 'Host': 'book.douban.com'
                         }
# sys.stdin.reconfigure(encoding='utf-8')

def start():
    log.info("Application started...")
    spider.get_single_book_details(URL, config.IS_TEST, save)

def save(x_content):
    log.info(x_content)


def get_single_book_details(book_url, is_test, save):

    log.info('Spider is processing: ' + book_url)

    if is_test:
        response = io.open(book_url, 'r')
    else:
        # urllib.request.install_opener(config.get_opener())
        # response = urllib.request.urlopen(book_url)
        # print(book_url)
        response = requests.get(book_url, headers=header)


    try:
        # bs = BeautifulSoup(pg_source, HTML_PARSER)
        # pg_source = response.read().decode('UTF8')
        # response.close()
        # os.putenv("PYTHONIOENCODING", 'UTF-8')
        # strr = os.getenv("PYTHONIOENCODING")

        # print(sys.stdin.encoding, sys.stdout.encoding)
        # print(strr)
        # print(requests.utils.get_encodings_from_content(response.text)[0])
        # print(response.encoding)
        # print(response.encoding)
        strr = response.text.encode('utf-8')
        bs = BeautifulSoup(strr, HTML_PARSER)
        # print(strr)

    except Exception as e:
        print(e)
    print('good')
    # return response

    # log.debug(bs)

    # book = {'link': book_url, 'title': bs.find('div', class_='sub-title').text,
    #         'review_count': bs.find('meta', itemprop='reviewCount').get('content'),
    #         'score': bs.find('meta', itemprop='ratingValue').get('content'),
    #         'image': bs.find('meta', property='og:image').get('content')}

    # book = {'link': book_url}

    # meta_list = bs.find('div', class_='sub-meta').text.split('/')

    # book['author'] = meta_list[0].strip()
    # if len(meta_list) > 1:
    #     book['publisher'] = meta_list[1].strip()
    # if len(meta_list) > 2:
    #     book['publish_year'] = meta_list[2].strip()
    #
    # content_list = bs.find('p', class_='section-intro_desc').text.split(' ')
    # filtered = [element.replace('\n', '') for element in content_list if element not in ['', '\n']]
    # book['intro'] = ''.join(line.replace('·', '').replace('·', '') for line in filtered)
    #
    # save(json.dumps(book, ensure_ascii=False))



# num1 = argv[1]
# num2 = argv[2]
# sum = int(num1) + int(num2) + 2
str = get_single_book_details(URL, False, False)
# print(str)
