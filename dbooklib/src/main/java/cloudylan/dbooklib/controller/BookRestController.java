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

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BookResponse> getALl() {
		List<BookFile> bookFiles = this.bookListService.getBookFileList();
		LOGGER.debug(bookFiles.toString());
		BookResponse response = new BookResponse();
		response.setBookFiles(bookFiles);

		return new ResponseEntity<BookResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Document>> getBookList() {
		List<Document> retVal = this.bookProvider.getAllMyBooks();

		return new ResponseEntity<List<Document>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> getBooks() {
		List<Document> retVal = this.bookProvider.getDetail();

		return new ResponseEntity<Document>(retVal.get(0), HttpStatus.OK);
	}

}
