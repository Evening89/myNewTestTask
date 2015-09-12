package properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	private static String pathToProperties = "src/properties/properties.props";
	
	private static FileReader reader;
	private static Properties properties;

	public static String implicitWait;
	
	public static String urlBase;
	public static String login,	password;
	
	public static String pathPicUpload, 
						 pathDocUpload, 
						 pathPicDownloadExpected, 
						 pathPicDownloadActual, 
						 pathDocDownloadActual;
	
	public static String uriCreate, uriUpdate, uriRead, uriDelete;
	
	static
	{
		try {
			reader = new FileReader(pathToProperties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		properties = new Properties();
		try {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		implicitWait = properties.getProperty("implicitWait");
		
		urlBase = properties.getProperty("url");
		login = properties.getProperty("login");
		password = properties.getProperty("password");

		String currentDir = System.getProperty("user.dir");		
		pathPicUpload = currentDir + properties.getProperty("pathPicUpload");
		pathDocUpload = currentDir + properties.getProperty("pathDocUpload");
		pathPicDownloadExpected = currentDir + properties.getProperty("pathPicDownloadExpected");
		pathPicDownloadActual = currentDir + properties.getProperty("pathPicDownloadActual");
		pathDocDownloadActual = currentDir + properties.getProperty("pathDocDownloadActual");
		
		uriCreate = properties.getProperty("uriCreate");
		uriUpdate = properties.getProperty("uriUpdate");
		uriRead = properties.getProperty("uriRead");
		uriDelete = properties.getProperty("uriDelete");
	}
}
