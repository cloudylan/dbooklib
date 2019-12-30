import pymongo
import logging
from bson.objectid import ObjectId


def save_book_detail(book_data):
    client = pymongo.MongoClient("mongodb://localhost:27017/")
    book_collection = client["librarydb"]["book"]

    inst = book_collection.insert_one(book_data)
    logging.debug(inst.inserted_id)
    return inst.inserted_id


def save_read_info_refer_id(read_id, book_id):
    logging.info("save_read_info_refer_id started...")
    client = pymongo.MongoClient("mongodb://localhost:27017/")
    book_collection = client["librarydb"]["user_read_info"]

    query = {"_id": ObjectId(read_id)}
    to_update = {"$set": {"bookReferId": str(book_id)}}

    book_collection.update_one(query, to_update)
    read_info = book_collection.find_one(query)
    logging.debug(read_info)

    return read_info





