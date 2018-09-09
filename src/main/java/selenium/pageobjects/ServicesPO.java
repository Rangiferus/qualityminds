package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ServicesPO extends BasePO {

    public ServicesPO(WebDriver driver) {
        super(driver);
    }

    public By btnMoreSelector = By.cssSelector("a.more");
    public WebElement btnMore() {
        return getWebElement(btnMoreSelector);
    }

}
