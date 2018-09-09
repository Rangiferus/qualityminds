import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import selenium.BaseTest;
import selenium.pageobjects.MenuBarPOC;
import selenium.pageobjects.ModalPanelPO;
import selenium.pageobjects.ServicesPO;
import selenium.utils.WaitAndVisibilityUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Coding challenge for Quality Minds
 */
public class QualityMindsTest extends BaseTest {

    private static final Logger LOG = LogManager.getFormatterLogger();
    private static final String WEBSITE_URL = "http://www.qualityminds.de";

    @Test
    public void testCase1() {
        webDriver.get(WEBSITE_URL);

        MenuBarPOC menuBar = new MenuBarPOC(webDriver);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(webDriver.findElement(By.tagName("html")).getAttribute("lang"))
                    .as("Page language version")
                    .isEqualTo("de");
            softAssertions.assertThat(menuBar.linkLangSwitcherDe().getAttribute("class"))
                    .withFailMessage("'de' language switcher is not active")
                    .contains("active");
        });

        LOG.debug("Click 'en' button");
        menuBar.linkLangSwitcherEn().click();
        new WaitAndVisibilityUtils(webDriver).waitForPageReload();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(webDriver.findElement(By.tagName("html")).getAttribute("lang"))
                    .as("Page language version")
                    .isEqualTo("en");
            softAssertions.assertThat(menuBar.linkLangSwitcherEn().getAttribute("class"))
                    .withFailMessage("'en' language switcher is not active")
                    .contains("active");
        });

    }

    @Test
    public void testCase2() {
        webDriver.get(WEBSITE_URL + "/en");

        MenuBarPOC menuBarPO = new MenuBarPOC(webDriver);
        LOG.debug("Hover [Services]");
        mouseHover(menuBarPO.menuItemServices());
        WaitAndVisibilityUtils baseWaitAndVisibilityActions = new WaitAndVisibilityUtils(webDriver);
        baseWaitAndVisibilityActions.waitForWebElement(menuBarPO.subMenuItemMobileTestingSelector, 5, "submenu [Mobile Testing]");
        LOG.debug("Click [Mobile Testing]");
        menuBarPO.subMenuItemMobileTesting().click();

        ServicesPO servicesPO = new ServicesPO(webDriver);
        baseWaitAndVisibilityActions.waitForWebElement(servicesPO.btnMoreSelector, 5, "button [more]");
        assertThat(menuBarPO.menuItemServices().getAttribute("class"))
                .withFailMessage("'Services' menu is not highlighted")
                .contains("active");

        LOG.debug("Click [more]");
        servicesPO.btnMore().click();
        ModalPanelPO modalPanelPO = new ModalPanelPO(webDriver);
        baseWaitAndVisibilityActions.waitForWebElement(modalPanelPO.h2Selector, 10, "panel [Contact]");
        assertThat(modalPanelPO.panelContent().getText())
                .as("Contact information")
                .contains("Ron Werner");

        LOG.debug("Close modal panel");
        modalPanelPO.btnCloseModal().click();
        assertThat(baseWaitAndVisibilityActions.waitForElementInvisibility(modalPanelPO.panelContentSelector, 5, "modal [Contact]"))
                .withFailMessage("Modal panel didn't close")
                .isTrue();
    }

    @Test
    public void testCase3() {
        webDriver.get(WEBSITE_URL + "/en");

        LOG.debug("Click [Contact] icon");
        new MenuBarPOC(webDriver).btnContact().click();
        ModalPanelPO modalPanelPO = new ModalPanelPO(webDriver);
        WaitAndVisibilityUtils baseWaitAndVisibilityActions = new WaitAndVisibilityUtils(webDriver);
        baseWaitAndVisibilityActions.waitForWebElement(modalPanelPO.h2Selector, 10, "panel [Contact us]");

        LOG.debug("Click [Submit]");
        modalPanelPO.btnSubmt().click();
        assertThat(baseWaitAndVisibilityActions.waitForWebElement(modalPanelPO.boxMessageErrorSelector, 10, "error box"))
                .withFailMessage("Error box was not displayed")
                .isTrue();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("Name, Surname field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("E-mail field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("Subject field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("Message field is required.");
        });

        LOG.debug("Set [Full Name] to: Aputsiaq Olsen");
        modalPanelPO.inputName().sendKeys("Aputsiaq Olsen");
        LOG.debug("Click [Submit]");
        modalPanelPO.btnSubmt().click();
        baseWaitAndVisibilityActions.waitForPageReload();
        assertThat(baseWaitAndVisibilityActions.waitForWebElement(modalPanelPO.boxMessageErrorSelector, 10, "error box"))
                .withFailMessage("Error box was not displayed")
                .isTrue();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .doesNotContain("Name, Surname field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("E-mail field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("Subject field is required.");
            softAssertions.assertThat(modalPanelPO.boxMessageError().getText())
                    .as("Warning message")
                    .contains("Message field is required.");
        });

        LOG.debug("Set [Email] to: XYZ@ABC@GL");
        modalPanelPO.inputEmail().sendKeys("XYZ@ABC@GL");
        LOG.debug("Click [Submit]");
        modalPanelPO.btnSubmt().click();
        baseWaitAndVisibilityActions.waitForPageReload();
        assertThat(baseWaitAndVisibilityActions.waitForWebElement(modalPanelPO.boxMessageErrorSelector, 10, "error box"))
                .withFailMessage("Error box was not displayed")
                .isTrue();
    }
}
