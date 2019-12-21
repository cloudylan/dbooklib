package cloudylan.dbooklib.model;

public class BookReadInfo {

	private String id;
	private String name;
	private String author;
	private String isbn;
	private Boolean isRead;
	private String category;
	private String date;
	private Integer pageNo;
	private String source;
	private String description;
	private String toSearch;

	public BookReadInfo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer page) {
		this.pageNo = page;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getToSearch() {
		return toSearch;
	}

	public void setToSearch(String toSearch) {
		this.toSearch = toSearch;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookReadInfo [id=").append(id).append(", name=").append(name).append(", author=").append(author)
				.append(", isbn=").append(isbn).append(", isRead=").append(isRead).append(", category=")
				.append(category).append(", date=").append(date).append(", pageNo=").append(pageNo).append(", source=")
				.append(source).append(", description=").append(description).append(", toSearch=").append(toSearch)
				.append("]");
		return builder.toString();
	}

}
