package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {


    @Test (groups = {"Regression", "Master"})
    public void verify_account_registration() {

        try {
            logger.info("Started ********* TC001_AccountRegistrationTest ***********");
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();

        /*
        - Below is how we are implementing log4j2 which has been created in the Base Class page
        - Alternatively we can use other options like debug, trace.....etc
         */
            logger.info("Clicked on MyAccount link...");

            hp.clickRegister();
            logger.info("Clicked on Register link...");

            AccountRegistrationPage regpage = new AccountRegistrationPage(driver);

            regpage.setFirstName(randomString().toUpperCase());
            regpage.setLastName(randomString().toUpperCase());
            regpage.setEmail(randomString() + "@gmail.com");
            regpage.setTelephone(randomNumber());

        /* We are storing the randomAlphaNumeric() value in a variable because if we call randomAlphaNumeric() twice,
        it would generate two different alphanumeric values which do not match with each other
         */
            String password = randomAlphaNumeric();
            regpage.setPassword(password);
            regpage.setConfirmPassword(password);

            regpage.setPrivacyPolicy();
            regpage.clickContinue();

            logger.info("Validating expected message......");
            String confirmationMessage = regpage.getConfirmationMessage();
            if (confirmationMessage.equalsIgnoreCase("Your Account Has Been Created!")) {
                Assert.assertTrue(true);
            } else {
            /*
            - To get detailed reports, we can use error and debug instead of Info
            - Normal case scenario, we use either Info (or) debug
            - In configuration file as well we need to use Debug if ew need to capture Debug logs
             */
                logger.error("Test failed....");
                logger.debug("Debug logs...");
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.fail();
        }
        logger.info("Finished ********* TC001_AccountRegistrationTest ***********");
    }
}