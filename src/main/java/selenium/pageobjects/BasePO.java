package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Methods used by all page objects
 */
public class BasePO {

    private WebDriver webDriver;

    public BasePO(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    protected WebElement getWebElement(By selector) {
        return webDriver.findElement(selector);
    }
}
