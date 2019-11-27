package cloudylan.dbooklib.model;

public class BookFile {
	private String name;
	private BookFormatType format;

	public BookFile(String fileName) {
		super();
		this.name = fileName.substring(0, fileName.lastIndexOf('.'));
		String formatString = fileName.substring(fileName.lastIndexOf('.') + 1);

		if (formatString.equalsIgnoreCase("mobi")) {
			this.format = BookFormatType.MOBI;
		} else if (formatString.equalsIgnoreCase("azw3")) {
			this.format = BookFormatType.AZW3;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BookFormatType getFormat() {
		return format;
	}

	public void setFormat(BookFormatType format) {
		this.format = format;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookFile [name=").append(name).append(", format=").append(format).append("]");
		return builder.toString();
	}

}
