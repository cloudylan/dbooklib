package cloudylan.dbooklib.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloudylan.dbooklib.db.mongo.BooksProvider;
import cloudylan.dbooklib.model.BookReadInfo;

@RestController
@RequestMapping("/library/rest")
public class BookRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

	@Autowired
	private BooksProvider bookProvider;

	@RequestMapping(value = "/books", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Document>> getBooks(@RequestBody BookReadInfo request)
	{
		LOGGER.info(request.toString());
		List<Document> retVal = this.bookProvider.getMyBooks(request);
		return new ResponseEntity<List<Document>>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/read/add", method = RequestMethod.POST)
	public ResponseEntity<List<Document>> insertBooks(@RequestBody BookReadInfo request)
	{
		LOGGER.info(request.toString());
		List<Document> retVal = new ArrayList<Document>();
		
		return new ResponseEntity(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> getBookDetail() {
		List<Document> retVal = this.bookProvider.getDetail();
		return new ResponseEntity<Document>(retVal.get(0), HttpStatus.OK);
	}


}
