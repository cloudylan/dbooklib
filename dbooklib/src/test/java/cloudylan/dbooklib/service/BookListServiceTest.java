package cloudylan.dbooklib.service;

import org.bson.Document;
import org.junit.Test;

import cloudylan.dbooklib.db.mongo.BookReadInfoProvider;
import junit.framework.TestCase;

public class BookListServiceTest extends TestCase {

	private BookFileService service;
	
	@Override
	public void setUp()
	{
		this.service = new BookFileService();
		this.service.setReadInfoProvider(new BookReadInfoProvider());
	}

	@Test
	public void testLocalBookGet()
	{
		System.out.println(this.service.getBookFileList());
		Document doc = this.service.loadBookInfos("dylan");
		System.out.print(doc);
	}
}
