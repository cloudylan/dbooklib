package cloudylan.dbooklib.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cloudylan.dbooklib.model.BookFile;

@Service
public class BookListService {

	private static final String FOLDER = "/Users/cloudy/Downloads/book";

	private static final String APPENDX_MOBI = ".mobi";

	private static final String APPENDIX_AZW = ".azw3";

	private static final Logger LOGGER = LoggerFactory.getLogger(BookListService.class);

	public static void main(String[] args) {
//		LOGGER.info(new BookListService().getBookFileList().toString());

	}

	public List<String> getBookList() {
		File folder = new File(FOLDER);
		String[] fileArray = {};
		if (folder.exists() && folder.isDirectory()) {
			fileArray = folder.list();
		}
		return Arrays.asList(fileArray).stream()
				.filter(name -> name.endsWith(APPENDX_MOBI) || name.endsWith(APPENDIX_AZW))
				.collect(Collectors.toList());
	}
	
	public List<BookFile> getBookFileList()
	{
		List<String> bookFileList = this.getBookList();
		return bookFileList.stream().map(nm -> new BookFile(nm)).collect(Collectors.toList());
	}

}
