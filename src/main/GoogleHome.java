import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class GoogleHome {

    public static WebDriver driver;


    public String baseUrl = "https://www.google.com/";

    //Locators
    By input_search = By.xpath("//div[@class='SDkEP']//input");
    By button_search = By.xpath("//input[@aria-label='Google Search']");
    By resultText = By.xpath("(//div[@id='oFNiHe']//span)[1]");

    @DataProvider(name="allLanguages_data")
    public Object[][] data() {
        return new Object[][]{
                {"ਪੰਜਾਬੀ","ਕੀ ਤੁਹਾਡਾ ਮਤਲਬ ਹੈ:"},
                {"English","Did you mean:"},
                {"हिन्दी","इसके परिणाम दिखाए जा रहे हैं"},
                {"বাংলা","আপনি কি বোঝাতে চেয়েছেন:"},
                {"తెలుగు","మీకు కావలసింది ఇదేనా:"},
                {"मराठी","तुम्हाला असे म्हणायचे आहे:"},
                {"தமிழ்","நீங்கள் குறிப்பிட்டது இதுவா:"}
        };
    }

    @Test(dataProvider = "allLanguages_data")
    public void testAllLanguages(String lang, String text) throws Exception {
        SoftAssert softAssert= new SoftAssert();
        String langLink = "//div[@id='SIvCob']/a[text()='"+ lang +"']";
        if (driver.findElements(By.xpath(langLink)).size()>0)
            driver.findElement(By.xpath(langLink)).click();
        driver.findElement(input_search).sendKeys("salemon");
        Thread.sleep(500);
        driver.findElement(button_search).click();
        verifyResultText(softAssert, text);
        driver.navigate().back();
        softAssert.assertAll();
    }

    public void verifyResultText(SoftAssert softAssert, String text) {
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(resultText)));
        String result = driver.findElement(resultText).getText();
        softAssert.assertEquals(result,text);
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
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterTest
    public void def() throws Exception {
        Thread.sleep(1000);
        driver.quit();
    }
}
