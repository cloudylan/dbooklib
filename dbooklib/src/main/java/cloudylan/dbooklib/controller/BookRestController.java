package cloudylan.dbooklib.controller;

import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloudylan.dbooklib.db.mongo.BooksProvider;
import cloudylan.dbooklib.domain.book.BookResponse;
import cloudylan.dbooklib.model.BookFile;
import cloudylan.dbooklib.service.BookListService;

@RestController
@RequestMapping("/library/rest")
public class BookRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

	@Autowired
	private BookListService bookListService;
	
	@Autowired
	private BooksProvider bookProvider;

	@RequestMapping(value = "/hello")
	public ResponseEntity<String> getHomePage() {
		return new ResponseEntity<String>("Hello Book.", HttpStatus.OK);
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookResponse> getBookList() {
		List<BookFile> bookFiles = this.bookListService.getBookFileList();
		LOGGER.debug(bookFiles.toString());
		BookResponse response = new BookResponse();
		response.setBookFiles(bookFiles);

		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	public ResponseEntity<BookResponse> getAllBooks()
	{
		List<Document> retVal = this.bookProvider.getAllBooks();
		
		return new ResponseEntity(retVal, HttpStatus.OK);
	}

}
