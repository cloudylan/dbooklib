import requests
from bs4 import BeautifulSoup
import dlibboot.config as config
import logging as log
import re
import selenium.webdriver as webdriver
import time
import io
from lxml import etree


HTML_PARSER = 'html.parser'
phantomjs_path: str = '/Users/cloudy/Tools/phantomjs-2.1.1-macosx/bin/phantomjs'
log.basicConfig(level=config.LOG_LEVEL)


def get_single_book_details(book_url, is_test):

    log.info('Spider is processing: ' + book_url)
    response = requests.get(book_url, headers=config.HEADER)

    bs = BeautifulSoup(response.text, HTML_PARSER)
    log.debug(bs)

    book = {'link': book_url, 'title': bs.find('div', class_='sub-title').text,
            'review_count': bs.find('meta', itemprop='reviewCount').get('content'),
            'score': bs.find('meta', itemprop='ratingValue').get('content'),
            'image': bs.find('meta', property='og:image').get('content')}
    meta_list = bs.find('div', class_='sub-meta').text.strip().split('/')

    index = len(meta_list) - 1
    book['publish_year'] = meta_list[index].strip()
    index = index - 1
    book['publisher'] = meta_list[index].strip()
    index = index - 1
    if index == 0:
        book['author'] = meta_list[index].strip()

    content_list = bs.find('p', class_='section-intro_desc').text.split(' ')
    filtered = [element.replace('\n', '') for element in content_list if element not in ['', '\n']]
    book['intro'] = ''.join(line.replace('·', '').replace('·', '') for line in filtered)

    if is_test:
        book['isTest'] = True

    fetch_img(book['image'])

    log.debug(book)

    return book


def get_book_details_with_selenium(book_url):
    log.info('Spider is processing with selenium: ' + book_url)

    opt = webdriver.ChromeOptions()
    opt.add_argument('--headless')
    opt.add_argument('--disable-gpu')
    driver = webdriver.Chrome(
        options=opt, executable_path='/Users/cloudy/Tools/chromedriver')

    time.sleep(2)
    driver.get(book_url)
    # bs = BeautifulSoup(driver.page_source, HTML_PARSER)

    book = {}
    author = driver.find_element_by_id('info')
    log.debug(book)

    return book


def fetch_img(url):
    file_name = re.findall('[0-9a-zA-Z]+\.jpg', url)

    if len(file_name) == 0:
        log.debug("Image name is incorrect.")
        return
    response = requests.get(url)
    img = response.content

    with open(config.PICS_FOLDER + file_name[0], 'wb') as f:
        f.write(img)
        f.close()

    log.debug('Image ' + file_name[0] + ' is saved.')

