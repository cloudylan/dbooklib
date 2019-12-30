package cloudylan.dbooklib.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloudylan.dbooklib.config.AppConfiguration;
import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import cloudylan.dbooklib.model.BookCrawlerRequest;
import cloudylan.dbooklib.model.BookReadInfo;
import cloudylan.dbooklib.service.BookFileService;

@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class BookRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

	@Autowired
	private BookReadInfoProvider bookProvider;

	@Autowired
	private BookFileService bookService;

	/**
	 * This operation returns book reading status search.
	 */
	@RequestMapping(value = "/books", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Document>> getBooks(@RequestBody BookReadInfo request) {
		LOGGER.info(request.toString());
		List<Document> retVal = this.bookProvider.getMyBooks(request);
		return new ResponseEntity<List<Document>>(retVal, HttpStatus.OK);
	}

	/**
	 * This operation proceed reading information persistence.
	 */
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

	/**
	 * This operation returns book details for a given book.
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> getBookDetail(@PathVariable String id) {
		Document retVal = this.bookProvider.getDetail(id);
		return new ResponseEntity<Document>(retVal, HttpStatus.OK);
	}

	/**
	 * This operation proceeds batch reading info for local kindle files.
	 */
	@RequestMapping(value = "/read/load", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Document> loadReadInfos() {
		Document doc = this.bookService.loadBookInfos();
		return new ResponseEntity<Document>(doc,
				doc.getBoolean("isSuccessful") ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * 
	 * This operation calls Python program to crawl book details from Douban books.
	 */
	@RequestMapping(value = "/bee/fetch", method = RequestMethod.POST)
	public String crawlBookDetail(@RequestBody BookCrawlerRequest request) throws IOException, InterruptedException {
		LOGGER.debug(request.toString());
		String[] cmdArr = new String[] { AppConfiguration.PYTHON_INTERPRETOR, AppConfiguration.PYTHON_MAIN,
				request.getReadId(), request.getLink() };
		Process process = Runtime.getRuntime().exec(cmdArr);
		InputStream is = process.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		String str = dis.readLine();
		process.waitFor();
		LOGGER.debug(str);
		return "hello " + str;
	}

}
