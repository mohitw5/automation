import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GoogleHome {

    public static WebDriver driver;
    Map<String, String> allLang = new HashMap<>();


    public String baseUrl = "https://www.google.com/";

    //Locators
    By input_search = By.xpath("//div[@class='SDkEP']//input");
    By button_search = By.xpath("//input[@aria-label='Google Search']");
    By resultText = By.xpath("(//div[@id='oFNiHe']//span)[1]");

    @Test
    public void test() throws Exception {
        allLang.put("English","Did you mean:");
        allLang.put("हिन्दी","क्या आप का मतलब यह था:");
        allLang.put("বাংলা","আপনি কি বোঝাতে চেয়েছেন:");
        allLang.put("తెలుగు","మీకు కావలసింది ఇదేనా:");
        allLang.put("मराठी","तुम्हाला असे म्हणायचे आहे:");
        allLang.put("தமிழ்","நீங்கள் குறிப்பிட்டது இதுவா:");
        allLang.put("ਪੰਜਾਬੀ","ਕੀ ਤੁਹਾਡਾ ਮਤਲਬ ਹੈ:");
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        testAllLanguages();
    }

    public void testAllLanguages() throws Exception {
        for(Map.Entry<String,String> set : allLang.entrySet()) {
            String langLink = "//div[@id='SIvCob']/a[text()='"+ set.getKey() +"']";
            if (driver.findElements(By.xpath(langLink)).size()>0)
                driver.findElement(By.xpath(langLink)).click();
            driver.findElement(input_search).sendKeys("salemon");
            Thread.sleep(500);
            driver.findElement(button_search).click();
            verifyResultText(set.getValue());
            driver.navigate().back();
        }
    }

    public void verifyResultText(String text) throws Exception{
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(resultText)));
        String result = driver.findElement(resultText).getText();
        Assert.assertEquals(result,text);
    }

    @BeforeTest()
    @Parameters("browser")
    public void init(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver","src//main//driver//chromedriver");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver","src//main//driver//geckodriver");
            driver = new FirefoxDriver();
        }
    }

    @AfterTest
    public void def() throws Exception {
        Thread.sleep(1000);
        driver.quit();
    }
}
