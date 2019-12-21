package cloudylan.dbooklib.db.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
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
import com.mongodb.client.result.UpdateResult;

import cloudylan.dbooklib.model.BookReadInfo;

@Component
public class BooksProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BooksProvider.class);

	private final static MongoDatabase MONGODB = buildDatabase();

	private final static BasicDBList UNREAD_LIST = new BasicDBList();

	static {
		UNREAD_LIST.add("");
		UNREAD_LIST.add("TODO");
	}

	public List<Document> getDetail() {
		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.BOOK.value()).find();
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		return docs;

	}

	public List<Document> getMyBooks(BookReadInfo info) {
		Document toFind = new Document();

		String toSearch = info.getToSearch();
		if (null != toSearch) {
			toFind.append("name", new Document("$regex", toSearch));
		}

		String category = info.getCategory();
		if (null != category) {
			toFind.append("type", category);
		}

		Boolean isRead = null;
		if (null != info.getIsRead()) {
			isRead = info.getIsRead();
		}

		if (info.getDate() != null) {
			toFind.append("year", info.getDate());
		} else if (null != isRead && isRead) {
			toFind.append("year", new Document("$nin", UNREAD_LIST));
		} else if (null != isRead && !isRead) {
			toFind.append("year", new Document("$in", UNREAD_LIST));
		}

		int pageNo = info.getPageNo() == null ? 0 : info.getPageNo() - 1;
		final int numPerPage = Integer.valueOf(MongoData.BOOKS_PER_PAGE.value());

		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).find(toFind)
				.sort(new Document("_id", -1)).skip(pageNo * numPerPage).limit(numPerPage);
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			Document doc = bookIterator.next();
			doc.put("_id", ((ObjectId) doc.get("_id")).toString());
			docs.add(doc);
		}

		LOGGER.info(new StringBuffer("Get records: ").append(docs.size()).toString());

		return docs;
	}

	public Document insertReadInfo(BookReadInfo info) {
		LOGGER.info("Performing read info creating.");
		Document toInsert = new Document("type", info.getCategory()).append("name", info.getName())
				.append("year", info.getDate()).append("source", info.getSource()).append("author", info.getAuthor())
				.append("description", info.getDescription());

		MONGODB.getCollection(MongoData.USER_READ_INFO.value()).insertOne(toInsert);
		return toInsert;
	}

	public Document updateReadInfo(BookReadInfo info) {
		LOGGER.info("Performing read info updating.");
		Document toUpdate = new Document("type", info.getCategory()).append("name", info.getName())
				.append("year", info.getDate()).append("source", info.getSource()).append("author", info.getAuthor())
				.append("description", info.getDescription());
		UpdateResult ur = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.replaceOne(eq("_id", new ObjectId(info.getId())), toUpdate);

		LOGGER.info(ur.toString());
		return toUpdate;
	}

	public Document getReadInfo(String id) {
		Document query = new Document("_id", new ObjectId(id));
		Document doc = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).find(query).first();
		if (doc != null) {
			doc.put("_id", doc.get("_id").toString());
		}
		LOGGER.info(new StringBuffer("Result for id ").append(id).append(" is ").append(doc).toString());

		return doc;
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
