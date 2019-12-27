from mobi import Mobi
import pprint

book = Mobi("test/CharlesDarwin.mobi")
book.parse()

for record in book:
    print(record)

pprint.pprint(book.config)
