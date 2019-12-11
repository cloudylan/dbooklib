package cloudylan.dbooklib.db.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Component
public class BooksProvider {
	
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
		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).find().skip(pageNo * numPerPage).limit(numPerPage);
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		return docs;

	}
	
	private static MongoDatabase buildDatabase()
	{
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(MongoData.CONNECTION_STR.value())).retryWrites(true)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase(MongoData.DB.value());
		
		return database;
	}
}
