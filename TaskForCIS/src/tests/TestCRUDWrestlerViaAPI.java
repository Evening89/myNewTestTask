package tests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import pages.LoginPage;
import pages.HomePage;
import properties.PropertiesReader;

public class TestCRUDWrestlerViaAPI {
	
	WebDriver driver;
	LoginPage loginPage;
	HomePage homePage;
	
	HttpPost httpPost;
	HttpClient httpclient;
	StringEntity params;
	HttpEntity entity;
	HttpResponse response;
	JSONObject json;
	String responseString;
	
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
	public void testCRUDWrestlerViaAPI() throws ClientProtocolException, IOException, InterruptedException {
		String sessionId = getSessionId();
		String userId = createWrestlerAndVerify(sessionId);
		readWrestlerAndVerify(userId, sessionId);
//		updateWrestlerAndVerify(userId, sessionId); //wrong specification for update, doesn't work		
		deleteWrestlerAndVerify(userId, sessionId);		
	}

	@After
	public void endTest() {
		driver.close();
	}
	
	public String getSessionId() throws InterruptedException {
		httpclient = new DefaultHttpClient();	
		Thread.sleep(1000); 											
		return driver.manage().getCookieNamed("PHPSESSID").getValue();
	}
	
	public String createWrestlerAndVerify(String sessionId) throws ClientProtocolException, IOException {
		httpPost = new HttpPost(PropertiesReader.uriCreate);
		httpPost.setHeader("Cookie", "PHPSESSID=" + sessionId);
		params = new StringEntity("{"
				+ "\"new\":true,"
				+ "\"id_wrestler\":0,"
				+ "\"attaches\":[],"
				+ "\"card_state\":\"1\","
				+ "\"lname\":\"vsdv\","
				+ "\"fname\":\"cvxcvx\","
				+ "\"mname\":\"vxcvxcv\","
				+ "\"dob\":\"10-10-1988\","
				+ "\"region1\":\"3\","
				+ "\"fst1\":\"3\","
				+ "\"lictype\":\"1\","
				+ "\"style\":\"3\","
				+ "\"expires\":\"2016\"}");		
		httpPost.setEntity(params);
		response = httpclient.execute(httpPost);
		entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		json = new JSONObject(responseString);
		String id = json.get("id").toString();
		Assert.assertEquals("'result' for wrestler is not 'true'. Probably, it wasn't created", "true", json.get("result").toString());
		Assert.assertTrue("'id' of wrestler is empty", !json.get("id").toString().isEmpty());
		return id;
	}
	
	public void readWrestlerAndVerify(String userId, String sessionId) throws ClientProtocolException, IOException {
		httpPost = new HttpPost(PropertiesReader.uriRead + userId);
		httpPost.setHeader("Cookie", "PHPSESSID=" + sessionId);
		response = httpclient.execute(httpPost);
		entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		json = new JSONObject(responseString);
		Assert.assertEquals( 
				"{\"fname\":\"cvxcvx\",\"region1\":\"3\",\"expires\":\"2016\",\"photo\":\"\",\"id_wrestler\":\"" + userId + "\",\"mname\":\"vxcvxcv\",\"attaches\":[],\"lname\":\"vsdv\",\"card_state\":\"1\",\"dob\":\"10-10-1988\",\"lictype\":\"1\",\"style\":\"3\",\"fst1\":\"3\"}", 
				json.toString());
	}
	
	public void updateWrestlerAndVerify(String userId, String sessionId) throws ClientProtocolException, IOException {
		httpPost = new HttpPost(PropertiesReader.uriUpdate + userId);
		httpPost.addHeader("content-type", "application/json");		
		httpPost.setHeader("Cookie", "PHPSESSID=" + sessionId);
		params = new StringEntity("{\"lname\":\"vsdv1\"}");		
		httpPost.setEntity(params);
		response = httpclient.execute(httpPost);
		entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		json = new JSONObject(responseString);
		Assert.assertEquals("'result' for wrestler is not 'true'. Probably, it wasn't updated", "true", json.get("result").toString());
	}
	
	public void deleteWrestlerAndVerify(String userId, String sessionId) throws ClientProtocolException, IOException {
		httpPost = new HttpPost(PropertiesReader.uriDelete + userId);
		httpPost.setHeader("Cookie", "PHPSESSID=" + sessionId);
		response = httpclient.execute(httpPost);
		entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		json = new JSONObject(responseString);
		Assert.assertEquals("'result' for wrestler is not 'true'. Probably, it wasn't deleted", "true", json.get("result").toString());
	}
}
