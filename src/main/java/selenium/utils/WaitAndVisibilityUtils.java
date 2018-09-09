package selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import selenium.BaseTest;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.util.Arrays.isNullOrEmpty;

/**
 * Utility methods for verifications of visibility and synchronizations between page actions.
 */
public class WaitAndVisibilityUtils {

    private WebDriver webDriver;

    private static final Logger LOG = LogManager.getFormatterLogger();

    public WaitAndVisibilityUtils(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private static void sleep(Duration duration) {
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(duration);
        } catch (InterruptedException e) {
            LOG.error("Putting WebDriver to sleep failed!\n", e);
        }
    }

    public static void sleep(int seconds) {
        sleep(new Duration(seconds, TimeUnit.SECONDS));
    }

    public static void sleepMillis(int miliseconds) {
        sleep(new Duration(miliseconds, TimeUnit.MILLISECONDS));
    }

    /**
     * Waits for element visibility with timeout.
     * @param elementDescription optional element description for log
     * @return true if element was found and is visible, false otherwise
     */
    public boolean waitForWebElement(By selector, int timeoutInSec, String... elementDescription) {
        String description;
        if (!isNullOrEmpty(elementDescription))
            description = elementDescription[0];
        else
            description = selector.toString();

        // WebDriverWait action consumes more time than it's needed to verify element invisibility,
        // so first try to check it using Selenium's Implicit Wait:
        if (isWebElementDisplayed(selector))
            return true;
        try {
            new WebDriverWait(webDriver, timeoutInSec).until(ExpectedConditions.visibilityOfElementLocated(selector));
            return true;

        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException | NullPointerException e) {
            LOG.warn("Element not visible after %d sec: %s", timeoutInSec, description);
            return false;
        }
    }

    private boolean isWebElementDisplayed(By selector) {
        boolean isDisplayed = false;

        // set Implicit Wait time to small value in order not to waste time for waiting for element to show up if it's not visible
        webDriver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        try {
            isDisplayed = webDriver.findElement(selector).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ignored) {
            // ignored
        }

        // return to original Implicit Wait time value
        webDriver.manage().timeouts().implicitlyWait(BaseTest.TIMEOUT_IMPLICIT_WAIT_SEC, TimeUnit.SECONDS);
        return isDisplayed;
    }


    /**
     * Waits for element invisibility with timeout.
     * @param elementDescription optional description for log
     * @return true if element is no longer visible, false otherwise
     */
    public boolean waitForElementInvisibility(By selector, int timeoutInSeconds, String... elementDescription) {
        String description;
        if (!isNullOrEmpty(elementDescription))
            description = elementDescription[0];
        else
            description = selector.toString();

        if (!isWebElementDisplayed(selector)) {
            return true;
        }

        // WebDriverWait action consumes more time than it's needed to verify element invisibility so let's do it own way
        long startTime = System.currentTimeMillis();
        while (isWebElementDisplayed(selector)) {
            if ((System.currentTimeMillis() - startTime)/1000 >= timeoutInSeconds) {
                LOG.warn("Element still visible after %d sec: %s", timeoutInSeconds, description);
                return false;
            }
        }
        return true;
    }

    public void waitForPageReload() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            sleep(1);
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assertions.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

}
