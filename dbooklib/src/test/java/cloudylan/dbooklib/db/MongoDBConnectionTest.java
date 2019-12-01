package cloudylan.dbooklib.db;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionTest {

	final static String FORMAT = "mongodb+srv://<username>:<password>@<cluster-address>/test?w=majority";
	final static String CONN_MONGO_STR = "mongodb://127.0.0.1:27017/librarydb";

	public void main(String[] args) {
		System.out.println("Test MongoDB Connection");

		ConnectionString connString = new ConnectionString(CONN_MONGO_STR);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("librarydb");

		FindIterable<Document> bookDocs = database.getCollection("book").find();
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		while (bookIterator.hasNext()) {
			System.out.println(bookIterator.next().toJson());
		}

	}
}
