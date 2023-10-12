package org.seleniumbrain.lab.selenium.driver.factory;

import io.cucumber.spring.ScenarioScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.selenium.driver.Labs;
import org.seleniumbrain.lab.utility.RetryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@ScenarioScope
public class DriverFactory {

    @Autowired
    private LocalWebDriver localWebDriver;

    @Autowired
    private GridWebDriver gridWebDriver;

    @Autowired
    private SauceLabWebDriver sauceLabWebDriver;

    @Autowired
    private PerfectoWebDriver perfectoWebDriver;

    public void initiateWebDriverSession() {
        String labName = SeleniumConfigReader.getTestLabName();
        log.info(MessageFormat.format("Initiating WebDriver Session for {0} - TestLab Environment", labName));

        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .run(() -> {
                    try {
                        switch (Labs.valueOf(labName.toUpperCase())) {
                            case LOCAL -> localWebDriver.createSession();

                            case GRID -> gridWebDriver.createSession();

                            case SAUCE_LAB -> sauceLabWebDriver.createSession();

                            case PERFECTO -> perfectoWebDriver.createSession();

                            case DOCKER -> {}
                        }
                    } catch (Exception e) {
                        this.quiteWebDriver();
                    }
                    return true;
                });
    }

    public WebDriver getDriver() {
        String labName = SeleniumConfigReader.getTestLabName();

        switch (Labs.valueOf(labName.toUpperCase())) {
            case LOCAL -> {
                return localWebDriver.getDriver();
            }

            case GRID -> {
                return gridWebDriver.getDriver();
            }

            case SAUCE_LAB -> {
                return sauceLabWebDriver.getDriver();
            }

            case PERFECTO -> {
                return perfectoWebDriver.getDriver();
            }

            case DOCKER -> {}
        }

        return null;
    }

    public String getCurrentWindow() {
        return this.getDriver().getWindowHandle();
    }

    public Set<String> getWindowsList() {
        return this.getDriver().getWindowHandles();
    }

    public void refreshPage() {
        log.info("Refreshing the page");

    }

    public void maximizeWindow() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().manage().window().maximize();
    }

    public void minimizeWindow() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().manage().window().minimize();
    }

    public void clearCookies() {
        if(this.getDriver() != null)
            this.getDriver().manage().deleteAllCookies();
    }

    public void clearCookies(String namedCookie) {
        if(this.getDriver() != null)
            this.getDriver().manage().deleteCookieNamed(namedCookie);
    }

    public void closeCurrentWindow() {
        if(Objects.nonNull(this.getDriver()))
            this.getDriver().close();
    }
    public void quiteWebDriver() {
        log.info("Attempting to quite all browser sessions");
        new RetryCommand<Boolean>(SeleniumConfigReader.getFailureRetryCount())
                .forceRetryIgnoringExceptions(() -> {
                    if(this.getDriver() != null)
                        this.getDriver().quit();
                    return true;
                });
    }

    @SneakyThrows
    public String getScreenshotAsImage(String imageName) {
        File imgSource = ((TakesScreenshot) this.getDriver()).getScreenshotAs(OutputType.FILE);
        File imgTarget = new File(imageName + ".png");
        FileUtils.copyFile(imgSource, imgTarget);
        log.info("screenshot is stored at : " + imgTarget.getAbsolutePath());
        return imgTarget.getAbsolutePath();
    }

    public byte[] getScreenshotInBytes() {
        if(this.getDriver() != null)
            return ((TakesScreenshot) this.getDriver()).getScreenshotAs(OutputType.BYTES);
        else
            return "Unable to take screenshot as WebDriver is null".getBytes();
    }

    @SneakyThrows
    public byte[] getRobotToolkitScreenshotInBytes() {
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
}
