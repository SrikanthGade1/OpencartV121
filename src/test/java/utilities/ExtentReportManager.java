package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import freemarker.template.SimpleDate;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testBase.BaseClass;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExtentReportManager implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;

    String repName;

    // testContext stores whatever method we have executed during the test, those details would be stored in the variable
    public void onStart(ITestContext testContext) {

        /*
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date dt = new Date(); // To create date in java we have special class called date
        String currentdatetimestamp = df.format(dt); // To generate date in df format we are using this line
        */

        // Instead of writing above three lines, we are writing them in a single line as below
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        // "Test-Report-" - prefix of the report and  ".html" is suffix of the report during report publishing
        repName = "Test-Report-" + timeStamp + ".html";

        // Below specifies location of the report
        // All the below should be set before we start test execution. Hence mentioned in onStart method
        sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);

        sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of the report
        sparkReporter.config().setReportName("opencart Functional Testing"); // Name of the report
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name")); // Gets user name dynamically
        extent.setSystemInfo("Environment", "QA");

        /*
        getCurrentXmlTest() - Provides information as of which xml file is executing the current test
        os value is provided in the xml file (that we are using to run the test) which we are capturing below
         */
        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        /*
        - Same as above comment, below line value is provided in the xml file (that we are using to run the test)
        which we are capturing below. It gets all the included groups
        - If we do not provide groups in the xml, it would not display any groups in the report
         */
        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        // If groups are available, below line adds group information to the report
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    public void onTestSuccess(ITestResult result) {
        /*
        - createTest would create a new entry in the test case
        - result captures result from test method
        - from the result, we would extract which class we hae executed and from that we are getting name of the class
        - With the above we are creating a new entry in the report
         */
        test = extent.createTest(result.getTestClass().getName());  // Creates a new entry in the report
        test.assignCategory(result.getMethod().getGroups()); // To display groups in the report
        test.log(Status.PASS, "Test case passed is: " + result.getName() + " got successfully executed"); // Updates status as pass/fail/skipped
    }

    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, "Test case failed is: " + result.getName());
        test.log(Status.FAIL, "Test case failed and reason for failure is: " + result.getThrowable().getMessage());

        /*
        - To attach screenshot in the report
        - We are enclosing it in try catch block because if the screenshot is not properly taken/stored/available, file
        would not be available and it would be trying to attach the screenshot to the report resulting in file not
        found exception due to which test case would fail
         */
        try {
            String imgPath = new BaseClass().captureScreen(result.getName());
            // Attaches screenshot to the report
            test.addScreenCaptureFromPath(imgPath);
        } catch (IOException e1) {
            // printStackTrace - Prints warning/exception message
            e1.printStackTrace();
        }
    }

    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, "Test case skipped is: " + result.getName());
        // Status.INFO - Provides information as of why the test method got skipped
        test.log(Status.INFO, result.getThrowable().getMessage());
    }

    /*
    - Below method is must and should
    - Without the below everything written above would not be updated in the report
    - flush() is a method used to ensure that all logs, data, and reports generated during the test execution are saved
     to the output file.
     */
    public void onFinish(ITestContext context) {
        extent.flush();

        /*
        - Below code is not required. But if we want to open the report automatically without going to the location of
        the report and manually opening, below code should be written
         */
        String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
        File extentReport = new File(pathOfExtentReport);

        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         - To send report in email automatically after test execution, follow below video:
         1:07:00
         https://www.youtube.com/watch?v=xeEVNVEVefM&list=PLUDwpEzHYYLtQzEEEldbjPAR-gnStv4sR&index=5&ab_channel=SDET-QA
         */
    }
}