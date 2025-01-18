package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {

    /*
     - Below test case returns 3 passed and 2 failed which is expected
     - Refer to video - https://youtu.be/2DSf3lLcw00?list=PLUDwpEzHYYLtQzEEEldbjPAR-gnStv4sR
     */
    @Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class, groups = "Datadriven")
    public void verify_loginDDT(String email, String pwd, String expectedResult) {

        logger.info("********* Starting TC003_LoginDDT *********");

        try {
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();

            LoginPage lp = new LoginPage(driver);
            lp.setEmail(email); // We need to specify the key value
            lp.setPassword(pwd);
            lp.clickLogin();

            MyAccountPage macc = new MyAccountPage(driver);
            boolean targetpage = macc.isMyAccountPageExists();

        /*
        Data is Valid   - Login Success - Test Pass - Logout
        Data is Invalid - Login Failed  - Test Failed

        Data is Invalid - Login Success - Test Failed  - Logout
        Data is Invalid - Login Failed  - Test Pass
        */

            if (expectedResult.equalsIgnoreCase("Valid")) {
                if (targetpage == true) {
                    Assert.assertTrue(true);
                    macc.clickLogout();
                } else {
                    Assert.assertTrue(false);
                }

                if (expectedResult.equalsIgnoreCase("Invalid")) {
                    if (targetpage == true) {
                        macc.clickLogout();
                        Assert.assertTrue(false);
                    } else {
                        Assert.assertTrue(true);
                    }
                }
            }

            logger.info("********* Finished TC003_LoginDDT *********");
        } catch (Exception e) {
            Assert.fail();
        }
    }
}