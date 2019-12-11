package cloudylan.dbooklib.db.mongo;

public enum MongoData {
	
	CONNECTION_STR("mongodb://127.0.0.1:27017/librarydb"),

	DB("librarydb"),

	BOOK("book"),

	USER_READ_INFO("user_read_info"),

	BOOKS_PER_PAGE("20");

	private String value;

	MongoData(String value)
	{
		this.value = value;
	}

	public String value()
	{
		return this.value;
	}
}
