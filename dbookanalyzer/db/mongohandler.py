import pymongo


def save(book_data):
    client = pymongo.MongoClient("mongodb://localhost:27017/")
    book_collection = client["librarydb"]["book"]

    book_data["isTest"] = True
    inst = book_collection.insert_one(book_data)
    print(inst)
    client.close()





