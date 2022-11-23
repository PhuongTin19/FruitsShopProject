package pages;

import helpers.ValidateUIHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class ProjectPage {

    private WebDriver driver;
    private ValidateUIHelpers validateUIHelpers;

    public ProjectPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(driver);
    }

    private String pageText = "Projects";
    private String url = "/projects/all_projects";

    //    Element at Projects page
    private By headerPageText = By.tagName("h1");

    private By addProjectBtn = By.xpath("//a[@title='Add project']");

    private By projectSearchInput = By.xpath("//input[@placeholder='Search']");

    public AddProjectPage addProject() {
        validateUIHelpers.verifyElementText(headerPageText, pageText);
        validateUIHelpers.clickElement(addProjectBtn);

        return new AddProjectPage(driver);
    }

    public void enterSearchValue(String value) {
        validateUIHelpers.setText(projectSearchInput, value);
    }

    public void checkSearchTableByColumn(int column, String value) {
        //Xác định số dòng của table sau khi search
        List<WebElement> row = driver.findElements(By.xpath("//table//tbody/tr"));
        int rowTotal = row.size(); //Lấy ra số dòng
        System.out.println("Số dòng tìm thấy: " + rowTotal);
        //Duyệt từng dòng
        for (int i = 1; i <= rowTotal; i++) {
            WebElement elementCheck = driver.findElement(By.xpath("//table//tbody/tr[" + i + "]/td[" + column + "]"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", elementCheck);

            System.out.print(value + " - ");
            System.out.println(elementCheck.getText());
            Assert.assertTrue(elementCheck.getText().toUpperCase().contains(value.toUpperCase()), "Dòng số " + i + " không chứa giá trị tìm kiếm.");
        }

    }

}
