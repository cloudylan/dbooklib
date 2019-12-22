package cloudylan.dbooklib.domain;

import java.util.List;

import cloudylan.dbooklib.model.BookFile;

public class BookResponse {
	List<BookFile> bookFiles;

	public List<BookFile> getBookFiles() {
		return bookFiles;
	}

	public void setBookFiles(List<BookFile> bookFiles) {
		this.bookFiles = bookFiles;
	}
	
	
}
