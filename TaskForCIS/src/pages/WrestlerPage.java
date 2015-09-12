package pages;

import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class WrestlerPage {
	
	WebDriver driver;

	@FindBy(css=".ng-isolate-scope.active .glyphicon-remove")
	WebElement closeBtn;
	
	@FindBy(css=".btn-success")
	WebElement tickBtn;

	@FindBy(css="button.btn-danger")
	WebElement deleteBtn;

	@FindBy(css=".modal-footer .btn-success")
	WebElement confirmDeleteBtn;
	
	@FindBy(xpath="//input[@placeholder='Last name']")
	WebElement lastnameInput;

	@FindBy(xpath="//input[@placeholder='First name']")
	WebElement firstnameInput;

	@FindBy(xpath="//input[@placeholder='Middle name']")
	WebElement middlenameInput;

	@FindBy(xpath="//input[@placeholder='Date of Birth']")
	WebElement dateOfBirthInput;

	@FindBy(xpath="//select[@required='required']/option[text()='Region']/..")
	WebElement regionSelect;

	@FindBy(xpath="//select[@required='required']/option[text()='FST']/..")
	WebElement fstSelect;

	@FindBy(xpath="//input[@placeholder='Trainer']")
	WebElement trainerInput;

	@FindBy(xpath="//select[@required='required']/option[text()='Style']/..")
	WebElement styleSelect;

	@FindBy(xpath="//select[@required='required']/option[text()='Age']/..")
	WebElement ageSelect;

	@FindBy(xpath="//select[@required='required']/option[text()='Year']/..")
	WebElement yearSelect;

	@FindBy(xpath="//input[@type='file' and @uploader='photoUploader']")
	WebElement uploadPhotoBtn;

	@FindBy(xpath="//input[@type='file' and @uploader='attachUploader']")
	WebElement uploadDocBtn;

	@FindBy(css=".photo-drop .center-block")
	public WebElement downloadPhotoBtn;

	@FindBy(css=".file-drop .ng-binding")
	public WebElement downloadDocBtn;

	@FindBy(xpath="//ico[@ng-click='deleteAttach($index)']")
	public WebElement deleteDocBtn;

	@FindBy(css=".file-drop tbody")
	public WebElement filesTable;
	
	public WrestlerPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void clickCloseTabButton() {
		closeBtn.click();
	}
	
	public void setLastname(String keys, boolean clear) {
		if (clear) lastnameInput.clear();
		lastnameInput.sendKeys(keys);
	}
	
	public void setFirstname(String keys, boolean clear) {
		if (clear) firstnameInput.clear();
		firstnameInput.sendKeys(keys);
	}
	
	public void setMiddlename(String keys, boolean clear) {
		if (clear) middlenameInput.clear();
		middlenameInput.sendKeys(keys);
	}
	
	public void setDateOfBirth(String keys, boolean clear) {
		if (clear) dateOfBirthInput.clear();
		dateOfBirthInput.sendKeys(keys);
	}
	
	public void setRegion(String keys) {
		new Select(regionSelect).selectByVisibleText(keys);
	}
	
	public void setFst(String keys) {
		new Select(fstSelect).selectByVisibleText(keys);
	}

	public void setTrainer(String keys, boolean clear) {
		if (clear) trainerInput.clear();
		trainerInput.sendKeys(keys);
	}
	
	public void setStyle(String keys) {
		new Select(styleSelect).selectByVisibleText(keys);
	}
	
	public void setAge(String keys) {
		new Select(ageSelect).selectByVisibleText(keys);
	}
	
	public void setYear(String keys) {
		new Select(yearSelect).selectByVisibleText(keys);
	}
//============================================================
    
	public String getLastname() {
		return ((JavascriptExecutor) driver)
				.executeScript("return document.getElementsByTagName('input')[1].value;").toString();
	}
	
	public String getFirstname() {
		return ((JavascriptExecutor) driver)
				.executeScript("return document.getElementsByTagName('input')[2].value;").toString();
	}
	
	public String getMiddlename() {
		return ((JavascriptExecutor) driver)
				.executeScript("return document.getElementsByTagName('input')[4].value;").toString();
	}
	
	public String getDateOfBirth() {
		return ((JavascriptExecutor) driver)
				.executeScript("return document.getElementsByTagName('input')[3].value;").toString();
	}
	
	public String getRegion() {
		return new Select(regionSelect).getFirstSelectedOption().getText();
	}
	
	public String getFst() {
		return new Select(fstSelect).getFirstSelectedOption().getText();
	}

	public String getTrainer() {
		return ((JavascriptExecutor) driver)
				.executeScript("return document.getElementsByTagName('input')[5].value;").toString();
	}
	
	public String getStyle() {
		return new Select(styleSelect).getFirstSelectedOption().getText();
	}
	
	public String getAge() {
		return new Select(ageSelect).getFirstSelectedOption().getText();
	}
	
	public String getYear() {
		return new Select( yearSelect).getFirstSelectedOption().getText();
	}
//===============================================================
	
	public void clickTick() {
		tickBtn.click();
	}

	public void clickDelete() {
		deleteBtn.click();
	}

	public void clickConfirmDeletion() {
		confirmDeleteBtn.click();
	}

	public HomePage deleteWrestler() {
		deleteBtn.click();
		confirmDeleteBtn.click();
		return new HomePage(driver);
	}

	public void uploadPhoto(String path) {
		uploadPhotoBtn.sendKeys(path);
	}

	public void uploadDocument(String path) {
		uploadDocBtn.sendKeys(path);
	}

	public void deleteDocument() {
		deleteDocBtn.click();
		try {
			Thread.sleep(2000); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setWrestlerMandatoryFields(HashMap<String, String> values, boolean clear) {
		setLastname(values.get("lastname"), clear);
		setFirstname(values.get("firstname"), clear);
		setMiddlename(values.get("middlename"), clear);
		setDateOfBirth(values.get("dateofbirth"), clear);
		setRegion(values.get("region"));
		setFst(values.get("FST"));
		setStyle(values.get("style"));
		setAge(values.get("age"));
		setYear(values.get("year"));
		clickTick();
	}
	
	public HomePage createNewWrestler(HashMap<String, String> values, boolean clear) {
		setWrestlerMandatoryFields(values, clear);
		clickCloseTabButton();
		return new HomePage(driver);
	}
	
	public HomePage createNewWrestlerWithFiles(HashMap<String, String> values, boolean clear, String pathPhoto, String pathDoc) {
		setWrestlerMandatoryFields(values, clear);
		uploadPhotoAndDoc(pathPhoto, pathDoc);
		clickCloseTabButton();
		return new HomePage(driver);
	}

	public HomePage updateWrestler(HashMap<String, String> values, boolean clear) {
		setLastname(values.get("lastname"), clear);
		setFirstname(values.get("firstname"), clear);
		setMiddlename(values.get("middlename"), clear);
		setDateOfBirth(values.get("dateofbirth"), clear);
		setRegion(values.get("region"));
		setFst(values.get("FST"));
		setTrainer(values.get("trainer"), clear);	
		setStyle(values.get("style"));
		setAge(values.get("age"));
		setYear(values.get("year"));
		clickTick();
		clickCloseTabButton();
		return new HomePage(driver);
	}
	
	public HashMap<String, String> getDataOfWrestler() {
		HashMap<String, String> data = new HashMap<String, String>() {{
		put("lastname", getLastname());
		put("firstname", getFirstname());
		put("middlename", getMiddlename());
		put("dateofbirth", getDateOfBirth());
		put("region", getRegion());
		put("FST", getFst());
		put("style", getStyle());
		put("age", getAge());
		put("year", getYear());
		put("trainer", getTrainer());
		}};
		return data;
	}
	
	public void uploadPhotoAndDoc(String pathPhoto, String pathDoc) {
		uploadPhoto(pathPhoto);
		uploadDocument(pathDoc);
	}
}
