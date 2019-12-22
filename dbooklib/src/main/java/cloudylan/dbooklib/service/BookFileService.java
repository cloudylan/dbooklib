package cloudylan.dbooklib.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import cloudylan.dbooklib.model.BookFile;
import cloudylan.dbooklib.model.BookReadInfo;

@Service
public class BookFileService {

	private static final String FOLDER = "/Users/cloudy/Downloads/test/";

	private static final String APPENDX_MOBI = ".mobi";

	private static final String APPENDIX_AZW = ".azw3";

	private static final String REMOVE_NAME = "SoBooKs.cc+\\+* *\\-* *\\+*";
	
	private static final String REMOVE_FORMAT = "[\\.azw3]*[\\.mobi]*";

	private static final String KINDLE = "Kindle";

	private static final Logger LOGGER = LoggerFactory.getLogger(BookFileService.class);

	@Autowired
	private BookReadInfoProvider readInfoProvider;

	private List<String> getBookList() {
		File folder = new File(FOLDER);
		String[] fileArray = {};

		if (folder.exists() && folder.isDirectory()) {
			fileArray = folder.list();
		}
		return Arrays.asList(fileArray).stream()
				.filter(name -> name.endsWith(APPENDX_MOBI) || name.endsWith(APPENDIX_AZW))
				.collect(Collectors.toList());
	}

	public Document loadBookInfos() {
		List<String> fileNameList = this.getBookList();
		Document doc = null;

		if (!fileNameList.isEmpty()) {
			doc = this.addReadInfos(fileNameList, KINDLE, true);
		} else {
			doc = new Document("updateSuccessful", false);
			doc.append("fileArchived", false).append("isSuccessful", true).append("updatedNumber", 0);
		}

		return doc;
	}

	public List<BookFile> getBookFileList() {
		List<String> bookFileList = this.getBookList();
		return bookFileList.stream().map(nm -> new BookFile(nm)).collect(Collectors.toList());
	}

	public Document addReadInfos(List<String> bookNames, String source, boolean isTest) {
		boolean isSuccessfull = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<BookReadInfo> infos = new ArrayList<BookReadInfo>();
		List<File> files = new ArrayList<File>();
		for (String name : bookNames) {
			BookReadInfo info = new BookReadInfo();
			String newName = name.contains(REMOVE_NAME) ? name.replaceFirst(REMOVE_NAME, "").replaceFirst(REMOVE_FORMAT, "") : name;
			info.setName(newName);
			info.setSource(source);
			info.setDescription(
					new StringBuffer("Kindle 添加日期").append(sdf.format(Calendar.getInstance().getTime())).toString());
			infos.add(info);

			files.add(new File(new StringBuffer(FOLDER).append(name).toString()));
		}
		this.readInfoProvider.insertManyReadInfo(infos, isTest);
		Document doc = new Document("updateSuccessful", true);
		isSuccessfull = true;

		try {
			for (File file : files) {
				if (file.exists()) {
					File newF = new File(new StringBuffer(FOLDER).append("archived/")
							.append(file.getName().replaceFirst(REMOVE_NAME, "")).toString());
					file.renameTo(newF);
				}

			}
			doc.append("fileArchived", true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			doc.append("fileArchived", false);
			isSuccessfull = false;
		}
		doc.append("isSuccessful", isSuccessfull);
		doc.append("updatedNumber", files.size());

		return doc;

	}

	public void setReadInfoProvider(BookReadInfoProvider readInfoProvider) {
		this.readInfoProvider = readInfoProvider;
	}

}
