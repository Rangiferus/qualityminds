package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Top menu bar page object component
 */
public class MenuBarPOC extends BasePO {

    public MenuBarPOC(WebDriver driver) {
        super(driver);
    }

    private By linkLangSwitcherDeSelector = By.cssSelector(".de.menu-de");
    public WebElement linkLangSwitcherDe() {
        return getWebElement(linkLangSwitcherDeSelector);
    }

    private By linkLanguageSwitcherEnSelector = By.cssSelector(".en.menu-en");
    public WebElement linkLangSwitcherEn() {
        return getWebElement(linkLanguageSwitcherEnSelector);
    }

    private By menuItemServicesSelector = By.xpath("//ul[@class = 'menu full']//a[text() = 'Services']");
    public WebElement menuItemServices() {
        return getWebElement(menuItemServicesSelector);
    }

    public By subMenuItemMobileTestingSelector = By.xpath("//ul[@class='menu']//a[contains(text(), 'Mobile')]"); // //a[text() = 'Mobile Testing'] doesn't work
    public WebElement subMenuItemMobileTesting() {
        return getWebElement(subMenuItemMobileTestingSelector);
    }

    private By btnContactSelector = By.cssSelector(".btn.contact");
    public WebElement btnContact() {
        return getWebElement(btnContactSelector);
    }
}
