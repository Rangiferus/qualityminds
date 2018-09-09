package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Modal panel (with various contents) page object
 */
public class ModalPanelPO extends BasePO {

    public ModalPanelPO(WebDriver driver) {
        super(driver);
    }

    public By panelContentSelector = By.cssSelector(".panel-pane.pane-page-content");
    public WebElement panelContent() {
        return getWebElement(panelContentSelector);
    }

    public By h2Selector = By.cssSelector(".panel-flexible h2");
    public WebElement h2() {
        return getWebElement(h2Selector);
    }

    private By btnCloseModalSelector = By.cssSelector(".overlay-close .inner");
    public WebElement btnCloseModal() {
        return getWebElement(btnCloseModalSelector);
    }

    public By boxMessageErrorSelector = By.cssSelector(".messages.error");
    public WebElement boxMessageError() {
        return getWebElement(boxMessageErrorSelector);
    }

    private By inputNameSelector = By.name("submitted[full_name]");
    public WebElement inputName() {
        return getWebElement(inputNameSelector);
    }

    private By inputEmailSelector = By.name("submitted[email]");
    public WebElement inputEmail() {
        return getWebElement(inputEmailSelector);
    }

    private By btnSubmitSelector = By.cssSelector("input[value='Submit']");
    public WebElement btnSubmt() {
        return getWebElement(btnSubmitSelector);
    }
}
