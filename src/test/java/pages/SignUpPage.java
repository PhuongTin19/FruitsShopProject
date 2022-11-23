package pages;

import helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SignUpPage {

    WebDriver driver;
    private ValidateUIHelpers validateUIHelpers;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(this.driver);
    }

    //  Element at Sign In page
    private By usernameInput = By.name("username");
    private By passwordInput = By.name("password");
    private By confirmPasswordInput = By.name("confirm");
    private By fullnameInput = By.name("fullname");
    private By phoneInput = By.name("phone");
    private By emailInput = By.name("email");
    private By addressInput = By.name("address");
    private By signupBtn = By.xpath("//button[@type='submit']");
    
    public SignUpPage() {
		// TODO Auto-generated constructor stub
	}
    public DashboardPage signUp(String usernameValue, String passwordValue, String confirmPasswordValue, String fullnameValue
    		, String phoneValue, String emailValue, String addressValue) {
        validateUIHelpers.waitForPageLoaded();
        validateUIHelpers.setText(usernameInput, usernameValue);
        validateUIHelpers.setText(passwordInput, passwordValue);
        validateUIHelpers.setText(confirmPasswordInput, confirmPasswordValue);
        validateUIHelpers.setText(fullnameInput, fullnameValue);
        validateUIHelpers.setText(phoneInput, phoneValue);
        validateUIHelpers.setText(emailInput, emailValue);
        validateUIHelpers.setText(addressInput, addressValue);
        validateUIHelpers.clickElement(signupBtn);
        return new DashboardPage(this.driver);
    }
}
