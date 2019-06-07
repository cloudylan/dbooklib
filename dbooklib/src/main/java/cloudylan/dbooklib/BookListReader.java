package cloudylan.dbooklib;

import java.io.File;


public class BookListReader {
	
	private final static String PATH = "/Users/cloudy/Downloads/book";
	
//	private final static Logger LOGGER = LoggerFactory.

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("test");
		listBooks();
	}
	
	public static void listBooks()
	{
		File target = new File(PATH);
		
		if (target.isDirectory())
		{
			String[] fileList = target.list();
			System.out.println(fileList);
		}
	}

}
