package testcases;

import helpers.ExcelHelpers;
import common.BaseSetup;
import pages.ChangePWPage;
import pages.SignInPage;
import pages.SignUpPage;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class unitTest {

    private WebDriver driver;
    private SignInPage signInPage;
    private SignUpPage signUpPage;
    private ChangePWPage changePWPage;
    private ExcelHelpers excel;

    @BeforeClass
    public void setupBrowser() {
        driver = new BaseSetup().setupDriver("chrome");
        excel = new ExcelHelpers();
    }

    @Test
    public void signInPageReadExcelDynamic() throws Exception {

        excel.setExcelFile("src/test/resources/Book1.xlsx", "Sheet1");

        signInPage = new SignInPage(driver);
        driver.get("http://localhost:8080/security/login");

        for (int i = 1; i < 6; i++) {
            signInPage.signIn(excel.getCellData("username", i), excel.getCellData("password", i));
            Thread.sleep(2000);
        }

        for (int i = 1; i < 6; i++) {
            excel.setCellData("PASS", i, 10);
        }
    }
    
    //Đổi mk
    @Test
    public void signUpPageReadExcelDynamic() throws Exception {

        excel.setExcelFile("src/test/resources/Book1.xlsx", "Sheet1");

        changePWPage = new ChangePWPage(driver);
        driver.get("http://localhost:8080/user/change-password");

        for (int i = 6; i < 11; i++) {
            changePWPage.changePW(excel.getCellData("password", i), excel.getCellData("newpassword", i), excel.getCellData("confirm", i));
            Thread.sleep(2000);
        }

        for (int i = 6; i < 11; i++) {
            excel.setCellData("PASS", i, 10);
        }
    }
    
    //Đăng ký
    @Test
    public void changePasswordPageReadExcelDynamic() throws Exception {

        excel.setExcelFile("src/test/resources/Book1.xlsx", "Sheet1");

        signUpPage = new SignUpPage(driver);
        driver.get("http://localhost:8080/user/register");

        for (int i = 11; i < 14; i++) {
            signUpPage.signUp(excel.getCellData("username", i), excel.getCellData("password", i), excel.getCellData("confirm", i)
            		, excel.getCellData("fullname", i), excel.getCellData("phone", i), excel.getCellData("email", i), excel.getCellData("address", i));
            Thread.sleep(2000);
        }

        for (int i = 11; i < 14; i++) {
            excel.setCellData("PASS", i, 10);
        }
    }

    @AfterClass
    public void closeBrowser() {
        driver.close();
    }

}
