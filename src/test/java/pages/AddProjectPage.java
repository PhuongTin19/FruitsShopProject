package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import helpers.ValidateUIHelpers;

public class AddProjectPage {

    private WebDriver driver;
    private Actions actions;
    private ValidateUIHelpers validateUIHelpers;

    public AddProjectPage(WebDriver driver) {
        this.driver = driver;
        validateUIHelpers = new ValidateUIHelpers(driver);
    }

    private String pageText = "Add project";
    private String url = "/projects/all_projects";

    //    Element at Add product page
    private By headerPageText = By.id("ajaxModalTitle");

    private By titleInput = By.id("title");
    private By clientDropdown = By.xpath("//label[@for='client_id']/following-sibling::div");
    private By searchClientInput = By.xpath("//div[@id='select2-drop']/div/input");
    private By desInput = By.id("description");
    private By startDateInput = By.id("start_date");
    private By deadLineInput = By.id("deadline");
    private By pricesInput = By.id("price");
    private By labelInput = By.xpath("//div[@id='s2id_project_labels']//input");

    private By saveBtn = By.xpath("//button[@type='submit']");

    public void saveProduct() throws InterruptedException {

        Assert.assertTrue(validateUIHelpers.verifyElementText(headerPageText, pageText) , "Không phải trang Add Product.");

        //Chổ này các bạn đọc data từ file Excel vào
        validateUIHelpers.setText(titleInput, "PD03");
        Thread.sleep(1000);
        validateUIHelpers.clickElement(clientDropdown);
        Thread.sleep(1000);
        validateUIHelpers.setText(searchClientInput, "out source");
        Thread.sleep(1000);
        actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(1000);
        validateUIHelpers.setText(desInput, "Auto Description");
        validateUIHelpers.setText(startDateInput, "2021-10-04");
        actions.sendKeys(Keys.ENTER).build().perform();
        validateUIHelpers.setText(deadLineInput, "2021-10-10");
        actions.sendKeys(Keys.ENTER).build().perform();
        validateUIHelpers.setText(pricesInput, "4000");
        Thread.sleep(1000);
        validateUIHelpers.setText(labelInput, "public");
        Thread.sleep(1000);
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(1000);
        validateUIHelpers.clickElement(saveBtn);
    }

}
