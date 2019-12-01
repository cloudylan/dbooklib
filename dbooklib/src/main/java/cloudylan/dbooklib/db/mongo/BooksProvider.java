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

	final static String CONN_MONGO_STR = "mongodb://127.0.0.1:27017/librarydb";

	public List<Document> getAllBooks() {

		ConnectionString connString = new ConnectionString(CONN_MONGO_STR);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("librarydb");

		FindIterable<Document> bookDocs = database.getCollection("book").find();
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		List<Document> docs = new ArrayList<Document>();
		while (bookIterator.hasNext()) {
			docs.add(bookIterator.next());
		}

		return docs;

	}
}
