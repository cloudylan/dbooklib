import requests
from bs4 import BeautifulSoup
import dlibboot.config as config
import logging as log
import re
import selenium.webdriver as webdriver
import time


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


def get_comments_by_page(driver, comments_url, ret_val):
    driver.get(comments_url)
    # log.debug(driver.page_source)
    pattern = re.compile(r'http\S+/\d+/')
    book_id = re.search(pattern, comments_url)

    try:
        comment_list = driver.find_elements_by_class_name('comment-item')
        for comment_item in comment_list:
            vote_count = comment_item.find_element_by_class_name('vote-count').text
            comment_info = comment_item.find_element_by_class_name('comment-info')
            user_element = comment_info.find_element_by_tag_name('a')
            user_link = user_element.get_attribute('href')
            user_name = user_element.text

            span_elements = comment_info.find_elements_by_tag_name('span')
            rating = span_elements[0].get_attribute('class')
            comment_date = span_elements[1].text
            content = comment_item.find_element_by_class_name('short').text

            ret_val.append({'book_refer_id':book_id, 'vote_count': vote_count, 'user': user_name, 'user_link': user_link, 'rating': rating,
                            'date': comment_date, 'content': content})
            return True
    except Exception as e:
        log.error(e)
        return False


def get_book_comments(url):
    log.debug("Start loading review comments...")

    comments_url = url + 'comments/'
    ret_val = []
    opt = webdriver.ChromeOptions()
    opt.add_argument('--headless')
    opt.add_argument('--disable-gpu')
    driver = webdriver.Chrome(
        options=opt, executable_path='/Users/cloudy/Tools/chromedriver')

    time.sleep(2)

    get_comments_by_page(driver, comments_url, ret_val)

    # driver.get(comments_url)
    # log.debug(driver.page_source)
    #
    # comment_list = driver.find_elements_by_class_name('comment-item')
    # for comment_item in comment_list:
    #     vote_count = comment_item.find_element_by_class_name('vote-count').text
    #     comment_info = comment_item.find_element_by_class_name('comment-info')
    #     user_element = comment_info.find_element_by_tag_name('a')
    #     user_link = user_element.get_attribute('href')
    #     user_name = user_element.text
    #
    #     span_elements = comment_info.find_elements_by_tag_name('span')
    #     rating = span_elements[0].get_attribute('class')
    #     comment_date = span_elements[1].text
    #     content = comment_item.find_element_by_class_name('short').text
    #
    #     ret_val.append({'vote_count': vote_count, 'user': user_name, 'user_link': user_link, 'rating':rating, 'date': comment_date, 'content': content})

    init_page = 2

    while True:
        page_url = comments_url + 'hot?p=' + str(init_page)
        result = get_comments_by_page(driver, page_url, ret_val)
        if not result:
            break
        init_page += 1

    driver.close()
    return ret_val


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


