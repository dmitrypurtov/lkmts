package scripts;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class LkMtsPage {
    private static final String PAGE_URL = "http://login.mts.ru/amserver/UI/Login?service=lk&goto=https://lk.ssl.mts.ru/";
    private static final String SETTING_SIGN_IN_PAGE_URL = "https://lk.mts.ru/settings/entrance";
    private static final String SERVICE_PAGE_URL = "https://lk.mts.ru/uslugi/podklyuchennye";

    public static class Locators {
        static By loginInputBy = By.xpath("//*[@id=\"phone\"]");
        static By passwordInputBy = By.xpath("//*[@id=\"password\"]");
        static By nextButtonBy = By.xpath("//*[@id=\"submit\"]");
        static By signInButtonBy = By.xpath("//button[contains(@class,\"btn_login\")]");
        static By closePopupButtonBy = By.xpath("//div[contains(@class,\"is-open\")]//div[contains(@class,\"popup__close\")]");

        static By changePasswordInputBy = By.xpath("//input[@id=\"new_password\"]");
        static By changePasswordConformInputBy = By.xpath("//input[@id=\"conf_password\"]");
        static By changePasswordButtonBy = By.xpath("//button");
        static By changePasswordOkBy = By.xpath("//button[contains(@class,\"btn_login\")]");

        static By uslugiMenuButtonBy = By.xpath("//section[@data-name=\"navMenuLeft\"]//a[contains(@href,\"uslugi\")]");
        static By uslugiConnectedTabBy = By.xpath("//*[@id=\"services-page-widget\"]/div/div[2]/div/div/div/div[1]/div/div[1]/div/div");
        static By uslugiConnectedFreeTabBy = By.xpath("//div[contains(text(),\"Платные\")]");
        static By uslugiConnectedNoFreeTabBy = By.xpath("//div[contains(text(),\"Бесплатные\")]");
        static By uslugiConnectedOffComformButtonBy = By.xpath("//app/mts-dialog//button[2]");
        static By uslugiConnectedListBy = By.xpath("//*//service-item[@id=\"services-page-widget\"]");

        static By uslugiAvailableTabBy = By.xpath("//*[@id=\"services-page-widget\"]/div/div[2]/div/div/div/div[1]/div/div[2]/div/div");
        static By uslugiAvailableAllTabBy = By.xpath("//button[contains(text(),\"Все услуги\")]");
        static By uslugiAvailableListBy = By.xpath("//*//service-item[@id=\"services-page-widget\"]");

        static By uslugiListFindTitleBy = By.xpath("div//div[contains(@class,\"parental-statistics-services__info-title\")]");
        static By uslugiListFindOffBy = By.xpath(".//div[contains(@class,\"parental-statistics-services__controls\")]//button");

        static By phoneButtonBy = By.xpath("//*[@id=\"ng-header__account-phone_desktop\"]");

        static By userButtonBy = By.xpath("//*[@id=\"profile-widget-app\"]/div[2]/div[2]");
        static By exitButtonBy = By.xpath("//*[@id=\"profile-widget-app\"]//button//span[2]");

        static By settingIframeBy = By.xpath("//iframe[@id=\"notifications-control-frame\"]");
        static By settingNotificationInputBy = By.xpath("//label[contains(@class, \"settings__notification-radio\")]/input");
        static By settingNotificationSpanBy = By.xpath("//label[contains(@class, \"settings__notification-radio\")]/span[1]");
        static By settingNotificationButtonBy = By.xpath("//button");
    }

    public LkMtsPage openPage() {
        open(PAGE_URL);
        return this;
    }

    public LkMtsPage signIn(String login, String password) {
        $(Locators.loginInputBy).setValue(login);
        $(Locators.passwordInputBy).setValue(password);
        $(Locators.signInButtonBy).click();
        $(Locators.signInButtonBy).shouldNotBe(Condition.visible);
        return this;

    }

    public LkMtsPage changePassword(String password) {
        sleep(10000);
        if ($(Locators.changePasswordInputBy).is(Condition.exist)) {
            $(Locators.changePasswordInputBy).setValue(password);
            $(Locators.changePasswordConformInputBy).setValue(password);
            $(Locators.changePasswordButtonBy).click();
            $(Locators.changePasswordOkBy).shouldBe(Condition.visible).click();
        }
        return this;
    }


    public LkMtsPage offSmsInSetting() {
        open(SETTING_SIGN_IN_PAGE_URL);
        sleep(10000);
        $(Locators.settingIframeBy).shouldBe(Condition.visible);
        switchTo().frame($(Locators.settingIframeBy));
        boolean isSelected = $(Locators.settingNotificationInputBy).isSelected();
        if (isSelected) {
            $(Locators.settingNotificationSpanBy).click();
            sleep(5000);
            $(Locators.settingNotificationButtonBy).scrollIntoView(true).click();
        }
        switchTo().defaultContent();
        return this;
    }

    public Boolean isAuthorized() {
        return $(Locators.phoneButtonBy).is(Condition.visible);
    }

    public LkMtsPage singOut(Integer sleapTime) {
        $(Locators.userButtonBy).click();
        $(Locators.exitButtonBy).click();
        $(Locators.userButtonBy).shouldNotBe(Condition.visible);
        sleep(sleapTime);
        return this;
    }

    public LkMtsPage closePopup() {
        if ($(Locators.closePopupButtonBy).is(Condition.visible)) {
            $(Locators.closePopupButtonBy).click();
        }
        return this;
    }

    public LkMtsPage swichToServices() {
        open(SERVICE_PAGE_URL);
        return this;
    }

    public Boolean isSwichToConnectedServices() {
        return $(Locators.uslugiConnectedTabBy).is(Condition.visible);
    }

    public LkMtsPage swichToConnectedServices() {
        $(Locators.uslugiConnectedTabBy).click();
        if ($(Locators.uslugiConnectedFreeTabBy).is(Condition.visible)) {
            $(Locators.uslugiConnectedFreeTabBy).click();
        }
        if ($(Locators.uslugiConnectedNoFreeTabBy).is(Condition.visible)) {
            $(Locators.uslugiConnectedNoFreeTabBy).click();
        }
        return this;
    }

    public Boolean isSwichToAvailableServices() {
        return $(Locators.uslugiAvailableTabBy).is(Condition.visible);
    }

    public LkMtsPage swichToAvailableServices() {
        $(Locators.uslugiAvailableTabBy).click();
        $(Locators.uslugiAvailableAllTabBy).click();
        return this;
    }

    public LkMtsPage offService(String name) {
        List<SelenideElement> services = $$(Locators.uslugiConnectedListBy);
        for (SelenideElement service : services) {
            String text = service.find(Locators.uslugiListFindTitleBy).text();
            if (text.toLowerCase().equals(name.toLowerCase())) {
                service.find(Locators.uslugiListFindOffBy).click();
                $(Locators.uslugiConnectedOffComformButtonBy).click();
                return this;
            }
            service.find(Locators.uslugiListFindTitleBy).scrollIntoView(true);
        }
        Assert.assertTrue(false, "Ошибка! Услуга не выключена так как не найденна");
        return this;
    }

    public Boolean isOffServiceInPage(String name) {
        List<SelenideElement> services = $$(Locators.uslugiConnectedListBy);
        for (SelenideElement service : services) {
            String text = service.find(Locators.uslugiListFindTitleBy).text();
            if (text.toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    public LkMtsPage onService(String name) {
        List<SelenideElement> services = $$(Locators.uslugiAvailableListBy);
        for (SelenideElement service : services) {
            String text = service.find(Locators.uslugiListFindTitleBy).text();
            if (text.toLowerCase().equals(name.toLowerCase())) {
                service.find(Locators.uslugiListFindOffBy).click();
                $(Locators.uslugiConnectedOffComformButtonBy).click();
                return this;
            }
            service.find(Locators.uslugiListFindTitleBy).scrollIntoView(true);
        }
        Assert.assertTrue(false, "Ошибка! Услуга не включенна так как не найденна");
        return this;
    }

    public Boolean isOnService(String name) {
        List<SelenideElement> services = $$(Locators.uslugiAvailableListBy);
        for (SelenideElement service : services) {
            String text = service.find(Locators.uslugiListFindTitleBy).text();
            if (text.toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
