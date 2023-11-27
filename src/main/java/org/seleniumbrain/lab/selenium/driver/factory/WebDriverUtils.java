package org.seleniumbrain.lab.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.cucumber.spring.ApplicationContextUtil;
import org.seleniumbrain.lab.cucumber.spring.configure.CucumberStepLog;
import org.seleniumbrain.lab.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.selenium.driver.WebDriverWaits;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.seleniumbrain.lab.utility.RetryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * It provides reusable methods using WebDriver instance.
 */
@Slf4j
@Component
@ScenarioScope
public class WebDriverUtils {

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private ScenarioState scenarioState;

    @Autowired
    private WebDriverWaits wait;

    public WebDriver getDriver() {
        return driverFactory.getDriver();
    }

    public static WebDriver getWebDriver() {
        return ApplicationContextUtil.getBean(DriverFactory.class).getDriver();
    }

    public boolean isWebDriverNull() {
        return Objects.isNull(this.getDriver());
    }

    public void launchUrl(String url) {
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .run(() -> {
                    this.getDriver().get(url);
                    wait.untilPageLoadComplete(60);
                    log.info("Launched url : " + url);
                    return true;
                });
    }

    public void launchUrlInNewWindow(String url) {
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .run(() -> {
                    this.getDriver().switchTo().newWindow(WindowType.WINDOW);
                    this.getDriver().get(url);
                    wait.untilPageLoadComplete(60);
                    log.info("Launched url in new window: " + url);
                    return true;
                });
    }


    public String getCurrentUrl() {
        return this.getDriver().getCurrentUrl();
    }

    public String getCurrentWindow() {
        return this.getDriver().getWindowHandle();
    }

    public Set<String> getWindowsList() {
        return this.getDriver().getWindowHandles();
    }

    public void refreshPage() {
        log.info("Refreshing the page");
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .run(() -> {
                    if (this.getDriver() != null) {
                        this.getDriver().navigate().refresh();
                        wait.untilPageLoadComplete();
                    }
                    return true;
                });
    }

    public void maximizeWindow() {
        if (Objects.nonNull(this.getDriver()))
            this.getDriver().manage().window().maximize();
    }

    public void minimizeWindow() {
        if (Objects.nonNull(this.getDriver()))
            this.getDriver().manage().window().minimize();
    }

    public void navigateBack() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().navigate().back();
    }

    public void navigateForward() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().navigate().forward();
    }

    public void fullScreen() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().manage().window().fullscreen();
    }

    public void clearCookies() {
        if (this.getDriver() != null)
            this.getDriver().manage().deleteAllCookies();
    }

    public void clearCookies(String namedCookie) {
        if (this.getDriver() != null)
            this.getDriver().manage().deleteCookieNamed(namedCookie);
    }

    public void closeCurrentWindow() {
        if (Objects.nonNull(this.getDriver()))
            this.getDriver().close();
    }

    public void quiteWebDriver() {
        log.info("Attempting to quite all browser sessions");
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .forceRetryIgnoringExceptions(() -> {
                    if (this.getDriver() != null)
                        this.getDriver().quit();
                    return true;
                });
    }

    public void attachScreenshot(String screenshotName) {
        wait.pause(1500);
        byte[] image = this.getScreenshotInBytes();
        CucumberStepLog stepLog = CucumberStepLog.builder()
                .caption(screenshotName)
                .img(image)
                .build();
        scenarioState.getStepLogs().add(stepLog);
        log.info("Attaching screenshot with caption '" + screenshotName + "'");
    }

    public void attachStepLogInfo(String logMsg) {
        CucumberStepLog log = CucumberStepLog.builder()
                .caption("Read Action Note: ")
                .textLog(logMsg)
                .build();
        scenarioState.getStepLogs().add(log);
    }

    @SneakyThrows
    public String getScreenshotAsImage(String imageName) {
        File imgSource = ((TakesScreenshot) this.getDriver()).getScreenshotAs(OutputType.FILE);
        File imgTarget = new File(PathBuilder.getOutputFolder("screenshot") + imageName + ".png");
        FileUtils.copyFile(imgSource, imgTarget);
        log.info("screenshot is stored at : " + imgTarget.getAbsolutePath());
        return imgTarget.getAbsolutePath();
    }

    public byte[] getScreenshotInBytes() {
        try {
            Objects.requireNonNull(this.getDriver(), "WebDriver can not be null while taking screenshot");
            return ((TakesScreenshot) this.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            try {
                return this.getRobotToolkitScreenshotInBytes();
            } catch (Exception ex) {
                return "Unable to take screenshot".getBytes();
            }
        }
    }

    @SneakyThrows
    private byte[] getRobotToolkitScreenshotInBytes() {
        System.setProperty("java.awt.headless", "false");
        BufferedImage img = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        return this.bufferedImageToByteArray(img);
    }

    @SneakyThrows
    private byte[] bufferedImageToByteArray(BufferedImage img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }

    public void attachElementScreenshot(WebElement element, String screenshotName) {
        wait.pause(1500);
        byte[] image = this.getElementScreenshotInBytes(element);
        CucumberStepLog log = CucumberStepLog.builder()
                .caption(screenshotName)
                .img(image)
                .build();
        scenarioState.getStepLogs().add(log);
    }

    private byte[] getElementScreenshotInBytes(WebElement element) {
        try {
            final byte[] screenshot = this.getScreenshotInBytes();
            final BufferedImage img = ImageIO.read(new ByteArrayInputStream(screenshot));

            System.out.println(element.getSize().getWidth());
            System.out.println(element.getSize().getHeight());

            System.out.println(element.getLocation().getX());
            System.out.println(element.getLocation().getY());

            // crop the image to focus on element
            // get dimensions (crop points)
            Point topLeft = element.getLocation();
            Point bottomRight = new Point(topLeft.getX() + element.getSize().getWidth(),
                    topLeft.getY() + element.getSize().getHeight());

            BufferedImage imageCropedAroundElement = img.getSubimage(topLeft.getX() - 60,
                    topLeft.getY() - 60,
                    bottomRight.getX() + 60,
                    bottomRight.getY() + 60);
            return this.bufferedImageToByteArray(imageCropedAroundElement);
        } catch (Exception e) {
            return "Unable to take screenshot".getBytes();
        }
    }
}
