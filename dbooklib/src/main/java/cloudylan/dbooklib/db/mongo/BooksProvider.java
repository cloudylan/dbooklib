package cloudylan.dbooklib.db.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBList;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import cloudylan.dbooklib.controller.BookRestController;
import cloudylan.dbooklib.model.BookReadInfo;

@Component
public class BooksProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BooksProvider.class);

	private final static MongoDatabase MONGODB = buildDatabase();

	public List<Document> getDetail() {

		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.BOOK.value()).find();
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		return docs;

	}

	public List<Document> getAllMyBooks(int pageNo) {

		final int numPerPage = Integer.valueOf(MongoData.BOOKS_PER_PAGE.value());
		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).find()
				.skip(pageNo * numPerPage).limit(numPerPage);
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		return docs;

	}

	public List<Document> getMyBooks(BookReadInfo info) {
		Document toFind = new Document();
		String category = info.getCategory();
		if (null != category) {
			toFind.append("type", category);
		}

		Boolean isRead = null;
		if (null != info.getIsRead()) {
			isRead = info.getIsRead();
		}

		BasicDBList values = new BasicDBList();
		values.add("");
		values.add("TODO");

		if (info.getDate() != null) {
			toFind.append("year", info.getDate());
		} else if (null != isRead && isRead) {
			toFind.append("year", new Document("$nin", values));
		} else if (null != isRead && !isRead) {
			toFind.append("year", new Document("$in", values));
		}

		int pageNo = info.getPageNo() == null ? 0 : info.getPageNo() - 1;
		final int numPerPage = Integer.valueOf(MongoData.BOOKS_PER_PAGE.value());

		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).find(toFind)
				.skip(pageNo * numPerPage).limit(numPerPage);
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		LOGGER.info(new StringBuffer("Get records: ").append(docs.size()).toString());

		return docs;
	}

	private static MongoDatabase buildDatabase() {
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(MongoData.CONNECTION_STR.value())).retryWrites(true)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase(MongoData.DB.value());

		return database;
	}

}
