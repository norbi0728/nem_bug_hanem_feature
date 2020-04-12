package neptun_data;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class NeptunDataHandler {
    public static List<String> getData(String neptun, String password){
        List<String> classesNames = new ArrayList<>(); //itt taroljuk a targyakat

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://neptun11.uni-pannon.hu/hallgato/Login.aspx");
            WebElement userInput = driver.findElement(By.name("user"));
            userInput.sendKeys(neptun);
            WebElement passwordInput = driver.findElement(By.name("pwd"));
            passwordInput.sendKeys(password + Keys.ENTER);

            WebElement classesBtn = wait.until(presenceOfElementLocated(By.id("mb1_Targyak")));
            classesBtn.click();
            WebElement takenClassesBtn = wait.until(presenceOfElementLocated(By.id("mb1_Targyak_Felvetttargyak")));
            takenClassesBtn.click();

            WebElement listBtn = wait.until(presenceOfElementLocated(By.id("upFilter_expandedsearchbutton")));
            listBtn.click();

            WebElement classesList = wait.until(presenceOfElementLocated(By.className("scrollablebody")));
            List<WebElement> classes = classesList.findElements(By.tagName("span"));
            for (WebElement webElement: classes) {
                classesNames.add(new String(webElement.getText().getBytes(Charset.forName("utf-8"))));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return classesNames;
    }
}
