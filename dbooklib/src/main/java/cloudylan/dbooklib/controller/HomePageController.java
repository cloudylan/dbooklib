package cloudylan.dbooklib.controller;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.StringUtils;

import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import cloudylan.dbooklib.model.BookFile;
import cloudylan.dbooklib.model.BookReadInfo;
import cloudylan.dbooklib.service.BookFileService;

@Controller(value = "HomePageController")
public class HomePageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

	@Autowired
	private BookFileService bookListService;

	@Autowired
	private BookReadInfoProvider bookProvider;

	/*
	 * This is a test page.
	 */
	@RequestMapping(value = "/index")
	public String test(Model mode) {
		List<BookFile> bookFiles = this.bookListService.getBookFileList();
		LOGGER.debug(bookFiles.toString());
		mode.addAttribute("booklist", bookFiles);

		return "index";
	}

	/**
	 * 
	 * This page returns analysis for reading.
	 */
	@RequestMapping(value = "/analysis")
	public String analysis(Model model) {
		LOGGER.debug(new StringBuffer("Analysis Starts for ").toString());

		Map<String, Integer> readStats = this.bookProvider.getStatisticByReadFlag();

		model.addAttribute("readStats", readStats);

		return "analysis";
	}

	/*
	 * 
	 * This page returns reading information for a given book reading id.
	 */
	@RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
	public String getReadInfo(@PathVariable String id, Model model) {

		LOGGER.info(new StringBuffer("Request Read Info for: ").append(id).toString());

		Document readInfoDoc = this.bookProvider.getReadInfo(id);
		BookReadInfo bi = new BookReadInfo();

		if (readInfoDoc != null) {
			bi.setId(readInfoDoc.getString("_id"));
			bi.setName(readInfoDoc.getString("name"));
			bi.setDate(readInfoDoc.getString("year"));
			bi.setCategory(readInfoDoc.getString("type"));
			bi.setAuthor(readInfoDoc.getString("author"));
			bi.setIsRead(readInfoDoc.getString("year") != null ? true : false);
			bi.setSource((readInfoDoc.getString("source") == null || "".equals(readInfoDoc.getString("source"))) ? "纸质"
					: readInfoDoc.getString("source"));
			bi.setDescription(readInfoDoc.getString("description"));
			bi.setBookReferId(readInfoDoc.getString("bookReferId"));
		}

		Document detail = null;
		if (!StringUtils.isEmpty(bi.getBookReferId())) {
			detail = this.bookProvider.getDetail(bi.getBookReferId());
		} else {
			detail = new Document();
		}

		model.addAttribute("readInfo", bi);
		model.addAttribute("bookDetail", detail);

		return "readdetail";
	}

	/*
	 * This page is for adding new book reading information.
	 */
	@RequestMapping(value = "/read/new", method = RequestMethod.GET)
	public String createReadInfo() {
		return "readdetail";
	}

	/*
	 * This is home page.
	 */
	@RequestMapping(value = "/")
	public String home(Model mode) {
		return "home";
	}

}
