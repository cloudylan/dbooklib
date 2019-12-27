import os

path = '/Users/cloudy/Downloads/book'
mobi_surfix = '.mobi'
azw_surfix = '.azw3'

file = os.listdir(path)
for b in file:
    if b.endswith(azw_surfix) or b.endswith(mobi_surfix):
        print(b)
