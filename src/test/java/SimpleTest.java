import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class SimpleTest {

    @Test
    public void checkTitle() {

        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        //Loads the page
        driver.get("http://localhost:8080/hello");

        //Have to explicitly wait because it takes time for compiled html to load
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(_driver -> !_driver.getTitle().isEmpty());

        //Gets the title now that the compiled html resources have loaded
        var title = driver.getTitle();

        //Checks whether the title matches
        Assertions.assertEquals("Hello World", title);
    }
}
