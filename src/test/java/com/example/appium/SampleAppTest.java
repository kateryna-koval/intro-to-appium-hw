package com.example.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.System.getenv;
import static org.testng.Assert.assertEquals;

public class SampleAppTest {
    private AppiumDriverLocalService server;
    private AppiumDriver<MobileElement> driver;

    @BeforeClass
    private void setUp() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String platform = getenv("APPIUM_DRIVER");
        platform = platform == null ? "ANDROID" : platform.toUpperCase();
        String path = System.getProperty("user.dir");

        if (platform.equals("ANDROID")) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
            capabilities.setCapability(MobileCapabilityType.APP, path + "/ApiDemos-debug.apk");

            server = new AppiumServiceBuilder().usingAnyFreePort().build();
            server.start();
            driver = new AndroidDriver<>(server, capabilities);

            ((AndroidDriver<MobileElement>) driver).startActivity(new Activity("io.appium.android.apis", ".view.TextFields"));
        } else {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.2");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCuiTest");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 8 Simulator (15.2)");
            capabilities.setCapability(MobileCapabilityType.UDID, "7969E1E8-138A-42E3-ABE1-2F4199629643");
            capabilities.setCapability(MobileCapabilityType.APP, path + "/TestApp.app.zip");

            server = new AppiumServiceBuilder().usingAnyFreePort().build();
            server.start();
            driver = new IOSDriver<>(server, capabilities);
        }
    }

    @Test
    public void textFieldTest() {
        PageView pageView = new PageView(driver);
        pageView.setTextField("test");

        assertEquals(pageView.getTextField(), "test", "Text field was not set");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        server.stop();
    }
}
