package pageObjects;

import com.beust.ah.A;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountRegistrationPage extends BasePage {

    public AccountRegistrationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='input-firstname']")
    WebElement txtFirstName;
    @FindBy(xpath = "//input[@id='input-lastname']")
    WebElement txtLastName;
    @FindBy(xpath = "//input[@id='input-email']")
    WebElement txtEmail;
    @FindBy(xpath = "//input[@id='input-telephone']")
    WebElement txtTelephone;
    @FindBy(xpath = "//input[@id='input-password']")
    WebElement txtPassword;
    @FindBy(xpath = "//input[@id='input-confirm']")
    WebElement txtPasswordConfirm;
    @FindBy(xpath = "//input[@value='Continue']")
    WebElement btnContinue;
    @FindBy(xpath = "//input[@name='agree']")
    WebElement chkdPolicy;
    @FindBy(xpath = "//h1[text()='Your Account Has Been Created!']")
    WebElement msgConfirmation;

    public void setFirstName(String fname) {
        txtFirstName.sendKeys(fname);
    }

    public void setLastName(String lname) {
        txtLastName.sendKeys(lname);
    }

    public void setEmail(String email) {
        txtEmail.sendKeys(email);
    }

    public void setTelephone(String telephone) {
        txtTelephone.sendKeys(telephone);
    }

    public void setPassword(String pwd) {
        txtPassword.sendKeys(pwd);
    }

    // Password and Confirm password are both same. So same parameters are being taken
    public void setConfirmPassword(String pwd) {
        txtPasswordConfirm.sendKeys(pwd);
    }

    public void setPrivacyPolicy(){
        chkdPolicy.click();
    }

    public void clickContinue() {
        // Sol 1
        btnContinue.click();

//        // Sol 2
//        btnContinue.submit();
//
//        // Sol 3
//        Actions act = new Actions(driver);
//        act.moveToElement(btnContinue).click().perform();
//
//        // Sol 4
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("arguments[0].click();", btnContinue);
//
//        // Sol 5
//        btnContinue.sendKeys(Keys.RETURN);
//
//        // Sol 6
//        WebDriverWait myWait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        myWait.until(ExpectedConditions.elementToBeClickable(btnContinue)).click();
    }

    public String getConfirmationMessage() {
        try {
            return msgConfirmation.getText();
        } catch (Exception e) {
            return (e.getMessage());
        }

    }

}