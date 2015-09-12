package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
	
	WebDriver driver;
	
	@FindBy(xpath="//input[@placeholder='Login']")
	WebElement loginInput;
	
	@FindBy(xpath="//input[@placeholder='Password']")
	WebElement passwordInput;
	
	@FindBy(xpath="//button[@type='submit']")
	WebElement loginButton;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void setLogin(String login) {
		loginInput.sendKeys(login);		
	}

	public void setPassword(String password) {
		passwordInput.sendKeys(password);
	}
	
	public void clickLogin() {
		loginButton.click();
	}
	
	public HomePage loginToStreamTv(String login, String password) {		
		this.setLogin(login);
		this.setPassword(password);
		this.clickLogin();
		return new HomePage(driver);				
	}
}
