package cloudylan.dbooklib.model;

public class BookCrawlerRequest {
	private String readId;
	private String link;
	private String user;

	public BookCrawlerRequest() {
		super();
	}

	public String getReadId() {
		return readId;
	}

	public void setReadId(String readId) {
		this.readId = readId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookCrawlerRequest [readId=").append(readId).append(", link=").append(link).append(", user=")
				.append(user).append("]");
		return builder.toString();
	}

}
