package settings;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class SelenideTool {
    private static final int LOOP_TIMEOUT = 200;
    private static final int MAX_ELEMENT_MOVING_TIMEOUT = 60000;

    public SelenideTool() {
        Configuration.timeout = 60000;
        //Configuration.browser = "chrome";
        WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(1280, 1024));
    }

    public static void deleteAllCookies() {
        RemoteWebDriver remoteWebDriver = ((RemoteWebDriver) getWebDriver());
        remoteWebDriver.manage().deleteAllCookies();
    }

    public SelenideTool(String browser) {
        Configuration.browser = browser;
    }

    public static void forcedRefresh() {
        String js = "location.reload();";
        executeJavaScript(js);
    }

    public static void waitTime(Integer time) {
        sleep(time);
    }

    public static void closeDriver() {
        if (hasWebDriverStarted())
            getWebDriver().quit();
    }

    public static SelenideElement getStoppedElement(SelenideElement element) throws InterruptedException {
        int loopCount = 0;
        Point point0 = element.getLocation();
        Point point1 = new Point(0, 0);
        while (loopCount * LOOP_TIMEOUT < MAX_ELEMENT_MOVING_TIMEOUT
                && !point0.equals(point1)) {
            point0 = point1;
            point1 = element.getLocation();
            Thread.sleep(LOOP_TIMEOUT);
            loopCount++;
        }
        return element;
    }
}
