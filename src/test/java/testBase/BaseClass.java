package testBase;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Parameters;

public class BaseClass {

    /*
    Why we need to make webdriver as static has been explained in below video
    1:20:18
    https://www.youtube.com/watch?v=xeEVNVEVefM&list=PLUDwpEzHYYLtQzEEEldbjPAR-gnStv4sR&index=5&ab_channel=SDET-QA
    Note: Test cases work only if we remove the static keyword. Need to check if driver can be made static
     */
    public WebDriver driver;
    /*
    Below is to add log4j to the base class. We need to add the variable logger to setup method before creation of driver
     */
    public Logger logger;

    // Below entry should be created to load config.properties file. Below, p is a variable
    public Properties p;

    /*
    If we start executing tests based on groups and if we do not include them next to before and after classes/methods,
    then setup, teardown.....etc will also not be executed
     */
    @BeforeClass(groups = {"Sanity", "Regression", "Master", "Datadriven"})
    // We need to pass the parameters from master.xml to below and take the same parameters next to setup paranthesis
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        /*
        - For loading config.properties file we need to use FileReader class. Alternatively, FileInputStream can also
        be used

         */
        FileReader file = new FileReader(".//src//test//resources//config.properties");
        p = new Properties();
        p.load(file);

        /*
        - In getLogger, we need to specify the class (From which class we need to generate the logs)
        - In the below we are using this.getClass() to get the class name dynamically during test execution
        - Below gets class name which is being executed, for that particular class gets the logger and stores it into
        variable logger
         */
        logger = LogManager.getLogger(this.getClass());

        if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            //os
            if (os.equalsIgnoreCase("windows")) {
                capabilities.setPlatform(Platform.WIN11);
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else {
                System.out.println("No matching os");
                return;
            }

            //browser
            switch (br.toLowerCase()) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    System.out.println("Invalid browser name....");
                    return;
            }

            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
        }

        if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
            // br that is browser name is passed from the master.xml file
            switch (br.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    System.out.println("Invalid browser name....");
                /*
                - Under default we are not writing break statement as its final statement and there is nothing else
                to execute
                - We are using return at the end to prevent tests from being executed. As browser itself is not correct,
                there is no need to go ahead with test execution
                 */
                    return;
            }
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appURL")); // Reading url from properties file
        driver.manage().window().maximize();
    }

    @AfterClass(groups = {"Sanity", "Regression", "Master", "Datadriven"})
    public void tearDown() {
        driver.quit();
    }

    public String randomString() {
        String generatedString = RandomStringUtils.randomAlphabetic(5);
        return generatedString;
    }

    public String randomNumber() {
        String generatedNumber = RandomStringUtils.randomNumeric(10);
        return generatedNumber;
    }

    public String randomAlphaNumeric() {
        // Below line can also be used
//        String generateRandomStringUtils.randomAlphanumeric(7);
        String generatedString = RandomStringUtils.randomAlphabetic(5);
        String generatedNumber = RandomStringUtils.randomNumeric(10);
        System.out.println("Generated alpha numeric value is: " + generatedString + "@" + generatedNumber);
        return generatedString + "@" + generatedNumber;
    }

    // tname is to provide some name to the screenshot
    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        // Below we are copying source file to target file by giving path as well as extension as .png as its a image
        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);

        sourceFile.renameTo(targetFile);
        // Below returns the path where target file is stored
        return targetFilePath;
    }
}
