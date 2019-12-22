package cloudylan.dbooklib.controller;

import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import cloudylan.dbooklib.model.BookFile;
import cloudylan.dbooklib.model.BookReadInfo;
import cloudylan.dbooklib.service.BookFileService;

@Controller(value = "HomePageController")
@RequestMapping("/library")
public class HomePageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

	@Autowired
	private BookFileService bookListService;

	@Autowired
	private BookReadInfoProvider bookProvider;

	@RequestMapping(value = "/index")
	public String test(Model mode) {
		List<BookFile> bookFiles = this.bookListService.getBookFileList();
		LOGGER.debug(bookFiles.toString());
		mode.addAttribute("booklist", bookFiles);

		return "index";
	}
	
	@RequestMapping(value = "/analysis/{action}")
	public String analysis(@PathVariable String action)
	{
		LOGGER.debug(new StringBuffer("Analysis Starts for ").append(action).toString());
		
		if ("author".equals(action))
		{
		}
		else if ("year".equals(action))
		{
		}
		else if("category".equals(action)){
		}
		else {
			
		}
		return "home";
	}

	@RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
	public String getReadInfo(@PathVariable String id, Model model) {

		LOGGER.info(new StringBuffer("Request Read Info for: ").append(id).toString());

		Document doc = this.bookProvider.getReadInfo(id);
		BookReadInfo bi = new BookReadInfo();

		if (doc != null) {
			bi.setId(doc.getString("_id"));
			bi.setName(doc.getString("name"));
			bi.setDate(doc.getString("year"));
			bi.setCategory(doc.getString("type"));
			bi.setAuthor(doc.getString("author"));
			bi.setIsRead(doc.getBoolean("isRead", false));
			bi.setSource((doc.getString("source") == null || "".equals(doc.getString("source"))) ? "纸质" : doc.getString("source"));
			bi.setDescription(doc.getString("description"));
		}
		model.addAttribute("readInfo", bi);

		return "readdetail";
	}

	@RequestMapping(value = "/read/new", method = RequestMethod.GET)
	public String createReadInfo() {
		return "readdetail";
	}

	@RequestMapping(value = "/")
	public String home(Model mode) {
		return "home";
	}

}
