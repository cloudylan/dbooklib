import requests
from bs4 import BeautifulSoup
import dlibboot.config as config
import logging as log
import re

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
    meta_list = bs.find('div', class_='sub-meta').text.split('/')

    book['author'] = meta_list[0].strip()
    if len(meta_list) > 1:
        book['publisher'] = meta_list[1].strip()
    if len(meta_list) > 2:
        book['publish_year'] = meta_list[2].strip()

    content_list = bs.find('p', class_='section-intro_desc').text.split(' ')
    filtered = [element.replace('\n', '') for element in content_list if element not in ['', '\n']]
    book['intro'] = ''.join(line.replace('·', '').replace('·', '') for line in filtered)

    fetch_img(book['image'])

    if is_test:
        book['isTest'] = True

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

    log.debug('Image ' + file_name[0] + ' is saved.')

