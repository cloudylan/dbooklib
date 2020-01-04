package cloudylan.dbooklib.db.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

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
public class BookReadInfoProvider {

	private static final String PIC_SVC = "http://localhost/pics/book/";
	private static final String IMAGE_NAME = "[0-9a-zA-Z]+\\.jpg";
	private static final Pattern pattern = Pattern.compile(IMAGE_NAME);

	private static final Logger LOGGER = LoggerFactory.getLogger(BookReadInfoProvider.class);

	private final static MongoDatabase MONGODB = buildDatabase();

	private final static BasicDBList UNREAD_LIST = new BasicDBList();

	static {
		UNREAD_LIST.add("");
		UNREAD_LIST.add("TODO");
		UNREAD_LIST.add(null);
	}

	/**
	 * 
	 * Get Book Detail from MongoDB.
	 * 
	 */
	public Document getDetail(String id) {
		Document retVal = new Document();
		Document toFind = new Document();
		toFind.put("_id", new ObjectId(id));
		FindIterable<Document> bookDocs = MONGODB.getCollection(MongoData.BOOK.value()).find(toFind);
		MongoCursor<Document> bookIterator = bookDocs.iterator();

		if (bookIterator.hasNext()) {
			retVal = bookIterator.next();
		}

		Matcher matcher = pattern.matcher(retVal.getString("image"));
		if (matcher.find()) {
			retVal.put("image", new StringBuffer(PIC_SVC).append(matcher.group(0)).toString());
		}

		return retVal;
	}

	/**
	 * 
	 * Get Read Informations based on Search Conditions.
	 */
	public List<Document> getMyBooks(BookReadInfo info) {
		Document toFind = new Document();

		String toSearch = info.getToSearch();
		if (null != toSearch) {
			Document[] toSearchCondition = {new Document("name", new Document("$regex", toSearch)),
					new Document("author", new Document("$regex", toSearch))};
			toFind.append("$or", Arrays.asList(toSearchCondition));
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

	/**
	 * 
	 * Insert Read Info.
	 * 
	 * @param info
	 * @param isTest
	 * @return the inserted value.
	 */
	public Document insertReadInfo(BookReadInfo info, boolean isTest) {
		LOGGER.info("Performing read info creating.");
		Document toInsert = new Document("type", info.getCategory()).append("name", info.getName())
				.append("year", info.getDate()).append("source", info.getSource()).append("author", info.getAuthor())
				.append("description", info.getDescription());

		if (isTest) {
			toInsert.append("isTest", true);
		}

		MONGODB.getCollection(MongoData.USER_READ_INFO.value()).insertOne(toInsert);
		return toInsert;
	}

	/**
	 * Update Single Read Information.
	 * 
	 * @param info
	 * @param isTest
	 * @return
	 */
	public Document updateReadInfo(BookReadInfo info, boolean isTest) {

		LOGGER.info("Performing read info updating.");

		Document toUpdate = new Document("type", info.getCategory()).append("name", info.getName())
				.append("year", StringUtils.isEmptyOrWhitespace(info.getDate()) ? null : info.getDate())
				.append("source", info.getSource()).append("author", info.getAuthor())
				.append("description", info.getDescription()).append("isNew", false);
		Document updateObj = new Document("$set", toUpdate);

		if (isTest) {
			toUpdate.append("isTest", true);
		}

		UpdateResult ur = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.updateOne(eq("_id", new ObjectId(info.getId())), updateObj);

		LOGGER.info(ur.toString());
		return toUpdate;
	}

	public void insertManyReadInfo(List<BookReadInfo> readInfos, boolean isNew) {
		LOGGER.info("Performing many read infos inserting.");
		List<Document> toInsertList = new ArrayList<Document>();
		for (BookReadInfo info : readInfos) {
			Document toInsert = new Document("type", info.getCategory()).append("name", info.getName())
					.append("year", info.getDate()).append("source", info.getSource())
					.append("author", info.getAuthor()).append("description", info.getDescription());

			if (isNew) {
				toInsert.append("isNew", true);
			}

			toInsertList.add(toInsert);
		}

		MONGODB.getCollection(MongoData.USER_READ_INFO.value()).insertMany(toInsertList);
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

	/**
	 * Get Analysis. Including actions: year etc...
	 * 
	 * @return
	 */
	public List<Document> getAnalysisByYear(Integer limit) {
		int resultLimit = 30;
		Document year = new Document("$group", new Document("_id", "$year").append("count", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("_id", -1));

		if (null != limit) {
			resultLimit = limit;
		}
		Document limit_pipe = new Document("$limit", resultLimit);

		Document[] pipeline = { year, sort, limit_pipe };
		List<Document> retVal = new ArrayList<Document>();
		MongoCursor<Document> aggIter = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.aggregate(Arrays.asList(pipeline)).iterator();

		while (aggIter.hasNext()) {
			retVal.add(aggIter.next());
		}

		return retVal;
	}

	public Map<String, Integer> getStatisticByReadFlag() {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		int alreadyRead = 0;
		Document readMatchs = new Document("$match", new Document("year", new Document("$ne", null)));
		Document year = new Document("$group", new Document("_id", "$year").append("count", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("_id", -1));
		Document[] pipeline = { readMatchs, year, sort };
		MongoCursor<Document> aggIter = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.aggregate(Arrays.asList(pipeline)).iterator();
		while (aggIter.hasNext()) {
			alreadyRead += aggIter.next().getInteger("count");
		}

		Document notReadMatchs = new Document("$match", new Document("year", new Document("$eq", null)));
		Document[] notPipeline = { notReadMatchs, year, sort };
		Document nonRead = MONGODB.getCollection(MongoData.USER_READ_INFO.value()).aggregate(Arrays.asList(notPipeline))
				.first();

		ret.put("read", alreadyRead);
		ret.put("notRead", nonRead.getInteger("count"));

		return ret;
	}

	public Map<String, List<Document>> getStatisticsByCatetory(List<String> years) {
		Map<String, List<Document>> retVal = new HashMap<String, List<Document>>();

		List<Document> states = new ArrayList<Document>();
		Document queryHistory = new Document("year", new Document("$regex", "(19|20)\\d{2}"));
		Document pipeHistoryMatch = new Document("$match", queryHistory);
		Document pipeHistoryGroup = new Document("$group",
				new Document("_id", "$type").append("count", new Document("$sum", 1)));
		Document pipeHistorySort = new Document("$sort", new Document("_id", 1));
		Document[] pipeline = { pipeHistoryMatch, pipeHistoryGroup, pipeHistorySort };
		MongoCursor<Document> mc = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.aggregate(Arrays.asList(pipeline)).iterator();
		while (mc.hasNext()) {
			states.add(mc.next());
		}
		retVal.put("history", states);

		for (String year : years) {
			List<Document> internal = new ArrayList<Document>();
			Document queryYear = new Document("year", new Document("$regex", year));
			Document pipeYear = new Document("$match", queryYear);
			Document pipeYearGroup = new Document("$group",
					new Document("_id", "$type").append("count", new Document("$sum", 1)));
			Document pipeYearSort = new Document("$sort", new Document("_id", 1));
			Document[] pipelineYear = { pipeYear, pipeYearGroup, pipeYearSort };
			MongoCursor<Document> mcYear = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
					.aggregate(Arrays.asList(pipelineYear)).iterator();
			while (mcYear.hasNext()) {
				internal.add(mcYear.next());
			}
			retVal.put(year, internal);
		}

		return retVal;

	}

	public List<Document> getAuthorStatistics() {
		List<Document> result = new ArrayList<Document>();
		String[] nonInList = {null, ""};
		Document matches = new Document("$match",
				new Document("author", new Document("$nin", Arrays.asList(nonInList))).append("type", new Document("$ne", "漫画")));
		Document group = new Document("$group",
				new Document("_id", "$author").append("count", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("count", -1));
		Document[] pipeline = { matches, group, sort };
		MongoCursor<Document> authorCursor = MONGODB.getCollection(MongoData.USER_READ_INFO.value())
				.aggregate(Arrays.asList(pipeline)).iterator();

		while (authorCursor.hasNext()) {
			result.add(authorCursor.next());
		}

		return result;
	}

	/**
	 * Build Database Instance with MongoDB Driver.
	 * 
	 * @return
	 */
	private static MongoDatabase buildDatabase() {
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(MongoData.CONNECTION_STR.value())).retryWrites(true)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase(MongoData.DB.value());

		return database;
	}

}
