package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	
	WebDriver driver;

	@FindBy(xpath="//input[@ng-model='searchFor']")
	WebElement searchInput;

	@FindBy(xpath="//button[@ng-click='searchWrestler(searchFor)']")
	WebElement searchBtn;
	
	@FindBy(xpath="//button[@ng-click='newWrestler()']")
	WebElement newButton;

	@FindBy(css=".table-striped")
	WebElement table;

	@FindBy(css=".table-striped tr td")
	List<WebElement> tableCells;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void setSearchPhrase(String keys) {
		searchInput.sendKeys(keys);
	}

	public void clickSearchButton() {
		searchBtn.click();
	}
	
	public WrestlerPage clickNew() {
		newButton.click();
		return new WrestlerPage(driver);
	}

	public WrestlerPage navigateToWrestler(String wrestler) {
		table.findElement(By.xpath("//td[text()='" + wrestler + "']")).click();
		return new WrestlerPage(driver);
	}
	
	public ArrayList<String> getTableValues() {
		ArrayList<String> texts = new ArrayList<String>();
		List<WebElement> allCells = tableCells;
		for(WebElement we : allCells) 
			texts.add(we.getText());
		return texts;
	}
	
	public void doSearchByPhrase(String keys) {
		searchInput.clear();
		setSearchPhrase(keys);
		clickSearchButton();
	}
}
