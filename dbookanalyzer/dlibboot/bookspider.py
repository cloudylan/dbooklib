import requests
from bs4 import BeautifulSoup
import dlibboot.config as config
import logging as log

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

    if is_test:
        book['isTest'] = True

    log.debug(book)

    return book


# def get_single_book_details_2(book_url, write_to_file, is_test, save):
#
#     log.info('Spider is processing: ' + book_url)
#
#     if is_test:
#         response = io.open(book_url, 'r')
#     else:
#         urllib.request.install_opener(config.get_opener())
#         response = urllib.request.urlopen(book_url)
#
#     bs = BeautifulSoup(response, HTML_PARSER)
#     log.debug(bs)
#
#     book = Book(book_url)
#     book.title = bs.find('div', class_='sub-title').text
#     meta_list = bs.find('div', class_='sub-meta').text.split('/')
#
#     # for div in divs:
#     book.author = meta_list[0].strip()
#     if len(meta_list) > 1:
#         book.publisher = meta_list[1].strip()
#     if len(meta_list) > 2:
#         book.publish_year = meta_list[2].strip()
#     book.review_count = bs.find('meta', itemprop='reviewCount').get('content')
#     book.score = bs.find('meta', itemprop='ratingValue').get('content')
#     book.image = bs.find('meta', property='og:image').get('content')
#     content_list = bs.find('p', class_='section-intro_desc').text.split(' ')
#     filtered = [element.replace('\n', '') for element in content_list if element not in ['', '\n']]
#     intro_step1 = ''.join(line.replace('·', '').replace('·', '') for line in filtered)
#     book.intro = intro_step1
#     # log.debug(str(book))
#
#     save(str(book))
#
#
#     bookfilepath = '/Users/cloudy/Data/book/bookfile'
#     #
#     # if write_to_file:
#     #     datahandler.save_to_file(bookfilepath + '/BookDetails', 'BookDetails', complete_data)
#     #     datahandler.save_to_file(bookfilepath + '/AlsoLikes', 'AlsoLikes', also_likes_txt)
#     #     datahandler.save_to_file(bookfilepath + '/Also_E_Likes', 'Also_E_Likes', also_e_likes)
#     #     datahandler.save_to_file(bookfilepath + '/Introduction', 'Introduction', intro)
#
#     return 'Y'



