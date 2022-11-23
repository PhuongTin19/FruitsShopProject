package pages;

import helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class DashboardPage {

    private WebDriver driver;
    private ValidateUIHelpers validateUIHelpers;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(driver);
    }

    // Element for Dashboard page

    private By projectsMenu = By.xpath("//ul[@id='sidebar-menu']//span[contains(text(), 'Projects')]");

    private By elementPageText = By.xpath("//span[text() = 'My open tasks']");

    private By elementDashboard = By.xpath("//span[normalize-space()='My open tasks']");

    private String url = "/dashboard";

    public ProjectPage openProjectsPage() {
        validateUIHelpers.waitForPageLoaded();

        Assert.assertTrue(validateUIHelpers.verifyElementExist(elementDashboard), "Element không tồn tại");

        Assert.assertTrue(validateUIHelpers.verifyUrl(url), "Không phải trang Dashboard.");
        Assert.assertTrue(validateUIHelpers.verifyElementText(elementPageText, "My open tasks"), "Không phải nội dung trang Dashboard");
        validateUIHelpers.clickElement(projectsMenu);
        return new ProjectPage(driver);
    }

}
