package cloudylan.dbooklib.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cloudylan.dbooklib.model.BookFile;
import cloudylan.dbooklib.service.BookListService;

@Controller
public class HomePageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

	@Autowired
	private BookListService bookListService;

	@RequestMapping(value = "/")
	public String home(Model mode) {
		List<BookFile> bookFiles = this.bookListService.getBookFileList();
		LOGGER.debug(bookFiles.toString());

		mode.addAttribute("booklist", bookFiles);
		return "index";
	}
}
