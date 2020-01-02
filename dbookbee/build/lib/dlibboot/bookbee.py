import requests
from bs4 import BeautifulSoup
import dlibboot.config as config
import logging as log
import re
# import selenium.webdriver as web_driver
import time


HTML_PARSER = 'html.parser'
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
    log.info('Spider is processing: ' + book_url)
    # response = requests.get(book_url, headers=config.HEADER)
    #
    # bs = BeautifulSoup(response.text, HTML_PARSER)
    # log.debug(bs)
    #
    # book = {'link': book_url, 'title': bs.find('div', class_='sub-title').text,
    #         'review_count': bs.find('meta', itemprop='reviewCount').get('content'),
    #         'score': bs.find('meta', itemprop='ratingValue').get('content'),
    #         'image': bs.find('meta', property='og:image').get('content')}
    # meta_list = bs.find('div', class_='sub-meta').text.strip().split('/')
    #
    # index = len(meta_list) - 1
    # book['publish_year'] = meta_list[index].strip()
    # index = index - 1
    # book['publisher'] = meta_list[index].strip()
    # index = index - 1
    # if index == 0:
    #     book['author'] = meta_list[index].strip()
    #
    # content_list = bs.find('p', class_='section-intro_desc').text.split(' ')
    # filtered = [element.replace('\n', '') for element in content_list if element not in ['', '\n']]
    # book['intro'] = ''.join(line.replace('·', '').replace('·', '') for line in filtered)
    #
    # log.debug(book)
    #
    # return book
    log.debug("Selenium Start Up...")
    # driver = web_driver.PhantomJS()
    # driver.get("https://book.douban.com/subject/7067916/")
    # time.sleep(3)
    # log.debug(driver.page_source)

    # driver.quit()


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

