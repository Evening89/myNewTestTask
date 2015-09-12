package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import helpers.FileDownloader;
import pages.LoginPage;
import pages.WrestlerPage;
import pages.HomePage;
import properties.PropertiesReader;

public class TestCRUDWrestler {
	
	WebDriver driver;
	LoginPage loginPage;
	HomePage homePage;
	WrestlerPage wrestlerPage;
	
	HashMap<String, String> valuesNewWrestler = new HashMap<String, String>()
	{{
		put("lastname", 	"Doe");
		put("firstname",	"John" + RandomStringUtils.randomAlphabetic(5));
		put("middlename",	"J");
		put("dateofbirth",	"01-01-1980");
		put("region",		"Kyivska");
		put("FST",			"Dinamo");
		put("style",		"FS");
		put("age",			"Senior");
		put("year",			"2015");
	}};

	HashMap<String, String> valuesUpdateWrestler = new HashMap<String, String>()
	{{
		put("lastname", 	"Doee");
		put("firstname",	"John" + RandomStringUtils.randomAlphabetic(5));
		put("middlename",	"JJ");
		put("dateofbirth",	"02-02-1990");
		put("region",		"Vynnitska");
		put("FST",			"Kolos");
		put("style",		"FW");
		put("age",			"Junior");
		put("year",			"2017");
	}};
	
	@Before
	public void setupTest() {		
		driver = new FirefoxDriver();
		driver.get(PropertiesReader.urlBase);
		driver.manage().window().maximize();
		loginPage = new LoginPage(driver);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(PropertiesReader.implicitWait), TimeUnit.SECONDS);
		homePage = loginPage.loginToStreamTv(PropertiesReader.login, PropertiesReader.password);
	}	
		
	@Test
	public void testCreateAndReadWrestler() {
		String searchPhrase = valuesNewWrestler.get("lastname") + " " + valuesNewWrestler.get("firstname") + " " + valuesNewWrestler.get("middlename");
		wrestlerPage = createAndNavigateToWrestler(homePage, searchPhrase);
		
		HashMap<String, String> valuesWrestlerActual = wrestlerPage.getDataOfWrestler(); 
		checkReflectedWrestlerDataEqualsToOriginal(valuesNewWrestler, valuesWrestlerActual);
		wrestlerPage.deleteWrestler();
	}

	@Test
	public void testUpdateWrestler() {	
		String searchPhrase = valuesNewWrestler.get("lastname") + " " + valuesNewWrestler.get("firstname") + " " + valuesNewWrestler.get("middlename");
		wrestlerPage = createAndNavigateToWrestler(homePage, searchPhrase);
		homePage = wrestlerPage.updateWrestler(valuesUpdateWrestler, true);
		
		searchPhrase = valuesUpdateWrestler.get("lastname") + " " + valuesUpdateWrestler.get("firstname") + " " + valuesUpdateWrestler.get("middlename");
		wrestlerPage = searchAndNavigateToWrestler(homePage, searchPhrase);
		HashMap<String, String> updatedValuesWrestlerActual = wrestlerPage.getDataOfWrestler(); 

		checkReflectedWrestlerDataEqualsToOriginal(valuesUpdateWrestler, updatedValuesWrestlerActual);
		wrestlerPage.deleteWrestler();
	}

	@Test
	public void testDeleteWrestler() {
		String searchPhrase = valuesNewWrestler.get("lastname") + " " + valuesNewWrestler.get("firstname") + " " + valuesNewWrestler.get("middlename");
		wrestlerPage = createAndNavigateToWrestler(homePage, searchPhrase);
		
		homePage = wrestlerPage.deleteWrestler();
		homePage.doSearchByPhrase(searchPhrase);		
		ArrayList<String> tableVals = homePage.getTableValues();
		boolean isDeletedWrestlerPresent = tableVals.contains(searchPhrase);
			
		Assert.assertFalse("Deleted wrestler '" + searchPhrase + "' is present in search", isDeletedWrestlerPresent);
	}
	
	public HomePage createNewWrestler(HomePage homePage) {
		wrestlerPage = homePage.clickNew();
		return wrestlerPage.createNewWrestler(valuesNewWrestler, false);	
	}
	
	public WrestlerPage searchAndNavigateToWrestler(HomePage homePage, String searchPhrase) {
		homePage.doSearchByPhrase(searchPhrase);		
		return homePage.navigateToWrestler(searchPhrase);
	}
	
	public WrestlerPage createAndNavigateToWrestler(HomePage homePage, String searchPhrase) {
		homePage = createNewWrestler(homePage);
		return searchAndNavigateToWrestler(homePage, searchPhrase);
	}
		
	@Test
	public void testUploadFiles() throws Exception {
		wrestlerPage = homePage.clickNew();
		
		homePage = wrestlerPage.createNewWrestlerWithFiles(valuesNewWrestler, false, PropertiesReader.pathPicUpload, PropertiesReader.pathDocUpload);
		String searchPhrase = valuesNewWrestler.get("lastname") + " " + valuesNewWrestler.get("firstname") + " " + valuesNewWrestler.get("middlename");
		wrestlerPage = searchAndNavigateToWrestler(homePage, searchPhrase);
		
		boolean isImageDisplayed = wrestlerPage.downloadPhotoBtn.isDisplayed();

		FileDownloader fileDownloader = new FileDownloader(driver);
		fileDownloader.downloadImage(wrestlerPage.downloadPhotoBtn, PropertiesReader.pathPicDownloadActual);
		String checksumPicExp = getChecksumOfFile(PropertiesReader.pathPicDownloadExpected);
		String checksumPicAct = getChecksumOfFile(PropertiesReader.pathPicDownloadActual);
		
		fileDownloader.downloadFile(wrestlerPage.downloadDocBtn, PropertiesReader.pathDocDownloadActual);
		String checksumDocExp = getChecksumOfFile(PropertiesReader.pathDocUpload);
		String checksumDocAct = getChecksumOfFile(PropertiesReader.pathDocDownloadActual);
		
		wrestlerPage.deleteDocument();
		boolean isDocTableEmpty = wrestlerPage.filesTable.getText().equals("");
		wrestlerPage.deleteWrestler();
		
		Assert.assertTrue("Image is not displayed", isImageDisplayed);
		Assert.assertEquals("Images' checksums are not equal", checksumPicExp, checksumPicAct);
		Assert.assertEquals("Documents' checksums are not equal", checksumDocExp, checksumDocAct);
		Assert.assertTrue("Document wasn't deleted. There's still data in documents' table", isDocTableEmpty);
	}
	

	@After
	public void endTest() {
		driver.close();
	}	
	
	String getChecksumOfFile(String fileName) throws IOException
	{
		FileInputStream fis = new FileInputStream(fileName);
		String checksum = DigestUtils.md5Hex(fis);
		fis.close();
		return checksum;
	}

	void checkReflectedWrestlerDataEqualsToOriginal(HashMap<String, String> valuesWrestlerExp, HashMap<String, String> valuesWrestlerAct)
	{
		boolean isValsEqual = true;
		LinkedList<String> mismatches = new LinkedList<String>();
		String message = "";
		
		Set<String> keys = valuesWrestlerExp.keySet();
		for (String key : keys) {
			if (!valuesWrestlerExp.get(key).equals(valuesWrestlerAct.get(key))) {
				isValsEqual = false;
				message = String.format("Mismatch in key '%1$s'. "
										+ "Expected: '%2$s'. "
										+ "Actual: '%3$s'", 
										key, valuesWrestlerExp.get(key), valuesWrestlerAct.get(key));
				mismatches.add(message);
			}				
		}		
		
		Assert.assertTrue("Currently reflected and original wrestler' data aren't equal. " + mismatches, isValsEqual);
	}	
}
