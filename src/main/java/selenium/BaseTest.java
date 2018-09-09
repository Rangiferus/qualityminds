package selenium;

import io.qameta.allure.Attachment;
import listeners.TestExecutionListener;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Common test methods.
 * All Selenium WebDriver test classes should extend this class.
 */
@Listeners({TestExecutionListener.class})
public class BaseTest {

    private static final Logger LOG = LogManager.getFormatterLogger();

    private static final long TIMEOUT_PAGE_LOAD_SEC = 60;
    public static final long TIMEOUT_IMPLICIT_WAIT_SEC = 10;
    private static final String PATH_ATTACHMENTS = "target/test-attachments/";
    public String browser;

    protected WebDriver webDriver; // can't be static to allow for parallel test execution!

    @Parameters("browser") // defined in TestNG suite
    @BeforeMethod(alwaysRun = true)
    public void initWebDriver(@Optional("firefox") String browser) { // if no param given then execute tests on Firefox
        this.browser = browser.toUpperCase();
        switch (browser.toLowerCase()) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", new File("src/test/resources/webdrivers/geckodriver_0.21.0.exe").getAbsolutePath());
                webDriver = new FirefoxDriver();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", new File("src/test/resources/webdrivers/chromedriver_2.40.exe").getAbsolutePath());
                webDriver = new ChromeDriver();
                break;
            case "chrome_headless":

            default:
                Assertions.fail("Browser %s not supported", browser);
        }
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT_IMPLICIT_WAIT_SEC, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(TIMEOUT_PAGE_LOAD_SEC, TimeUnit.SECONDS);

        webDriver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());
        }
        webDriver.quit();
    }

    @Attachment(value = "Page screenshot", type = "image/png") // to Allure report
    private byte[] takeScreenshot(String testName) { // byte[] required for Jenkins test result viewer
        byte[] screenshot = null;
        try {
            screenshot = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES);

            new File(PATH_ATTACHMENTS).mkdirs();
            // result.getName() returns name of test case
            String screenshotFilePath = PATH_ATTACHMENTS + testName + "-" + browser + ".png";
            File screenshotFile = new File(screenshotFilePath);
            FileUtils.writeByteArrayToFile(screenshotFile, screenshot);
            LOG.error("[[ATTACHMENT|" + screenshotFile.getAbsolutePath() + "]]"); // for Jenkins test result viewer
            System.out.println(screenshotFile.getAbsolutePath()); // for quick copy file path to open and view
        } catch (Exception e) {
            LOG.error("Could not take screenshot");
            e.printStackTrace();
        }
        return screenshot;
    }

    protected void mouseHover(WebElement element) {
        new Actions(webDriver).moveToElement(element).build().perform();
    }
}