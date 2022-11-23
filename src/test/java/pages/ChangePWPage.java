package pages;

import helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ChangePWPage {

    WebDriver driver;
    private ValidateUIHelpers validateUIHelpers;

    public ChangePWPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(this.driver);
    }

    //  Element at Sign In page
    private By oldPasswordInput = By.name("oldPassword");
    private By newPasswordInput = By.name("newPassword");
    private By passwordInput = By.name("password");
    private By signinBtn = By.xpath("//button[@type='submit']");
    
    public ChangePWPage() {
		// TODO Auto-generated constructor stub
	}
    public DashboardPage changePW(String oldPasswordValue,String newPasswordValue ,String passwordValue) {
        validateUIHelpers.waitForPageLoaded();
        validateUIHelpers.setText(oldPasswordInput, oldPasswordValue);
        validateUIHelpers.setText(newPasswordInput, newPasswordValue);
        validateUIHelpers.setText(passwordInput, passwordValue);
        validateUIHelpers.clickElement(signinBtn);
        return new DashboardPage(this.driver);
    }
}
