package cloudylan.dbooklib.controller;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
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

import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import cloudylan.dbooklib.model.BookReadInfo;
import cloudylan.dbooklib.service.BookFileService;

@RestController
@RequestMapping("/library/rest")
public class BookRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

	@Autowired
	private BookReadInfoProvider bookProvider;

	@Autowired
	private BookFileService bookService;

	private final static boolean isTest = true;

	@RequestMapping(value = "/books", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Document>> getBooks(@RequestBody BookReadInfo request) {
		LOGGER.info(request.toString());
		List<Document> retVal = this.bookProvider.getMyBooks(request);
		return new ResponseEntity<List<Document>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/read/save", method = RequestMethod.POST)
	public ResponseEntity<Document> insertBooks(@RequestBody BookReadInfo request) {

		LOGGER.info(request.toString());

		Document result = null;
		if (request.getId() != null && !"".equals(request.getId())) {
			result = this.bookProvider.updateReadInfo(request, false);
			result.append("_id", request.getId());
		} else {
			result = this.bookProvider.insertReadInfo(request, true);
			result.put("_id", ((ObjectId) result.get("_id")).toString());
		}

		return new ResponseEntity<Document>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> getBookDetail() {
		List<Document> retVal = this.bookProvider.getDetail();
		return new ResponseEntity<Document>(retVal.get(0), HttpStatus.OK);
	}

	@RequestMapping(value = "/read/load", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> loadReadInfos() {
		Document doc = this.bookService.loadBookInfos();
		return new ResponseEntity<Document>(doc,
				doc.getBoolean("isSuccessful") ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED);
	}

}
