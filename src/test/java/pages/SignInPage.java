package pages;

import helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SignInPage {

    WebDriver driver;
    private ValidateUIHelpers validateUIHelpers;

    public SignInPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(this.driver);
    }

    //  Element at Sign In page
    private By usernameInput = By.name("username");
    private By passwordInput = By.name("password");
    private By signinBtn = By.xpath("//button[@type='submit']");
    
    public SignInPage() {
		// TODO Auto-generated constructor stub
	}
    public DashboardPage signIn(String emailValue, String passwordValue) {
        validateUIHelpers.waitForPageLoaded();
        validateUIHelpers.setText(usernameInput, emailValue);
        validateUIHelpers.setText(passwordInput, passwordValue);
        validateUIHelpers.clickElement(signinBtn);
        return new DashboardPage(this.driver);
    }
}
