package com.chen.autocases;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class TestDemo {
    private static Logger log = Logger.getLogger(TestDemo.class);
    public WebDriver driver;
    /*
    @param
        import java.util.HashMap;
        import java.util.Map;
        options.addArguments("--disable-popup-blocking"); // 禁用阻止弹出窗口
        options.addArguments("no-sandbox"); // 启动无沙盒模式运行
        options.addArguments("disable-extensions"); // 禁用扩展
        options.addArguments("no-default-browser-check"); // 默认浏览器检查
        Map<String, Object> prefs = new HashMap();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);// 禁用保存密码提示框

        */

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/chrome/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(Boolean.FALSE);
        //options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized"); // 启动时自动最大化窗口
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("http://www.baidu.com");
        log.info("用例开始执行------");
    }
    @AfterClass
    public void afterClass(){
            log.info("用例结束后运行----");
            driver.quit();
          }

    @Test
    public void testcase1(){
        log.info("testcase1");
        Assert.assertTrue(true);

    }
    @Test
    public void testcase2(){
        Assert.assertTrue(true);
        log.debug("testcase2");
    }
}