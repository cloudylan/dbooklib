import logging

HTML_PARSER = 'html.parser'
LOG_LEVEL = logging.DEBUG
IS_TEST = False
PICS_FOLDER = '/Users/cloudy/Java/tomcat/apache-tomcat-9.0.0.M9/webapps/pics/book/'

header_user_agent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) " \
                    "Chrome/68.0.3440.106 Mobile Safari/537.36"
client_id_label = 'X-DevTools-Emulate-Network-Conditions-Client-Id'
client_id = ''
cookie = ''

HEADER = {'User-agent': header_user_agent,
          'Accept':
              'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
          }
