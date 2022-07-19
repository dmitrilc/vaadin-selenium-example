import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTestIT {

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
        assertEquals("Hello World", title);

        //Ends the browser session
        driver.quit();
    }

    @Test
    public void routeSwitch(){
        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        //Loads the page
        driver.get("http://localhost:8080");

        //Have to explicitly wait because it takes time for compiled html to load
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(_driver -> !_driver.getTitle().isEmpty());

        //Clicks on the About button
        driver.findElement(By.linkText("About")).click();

        //Wait for new page to load
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(_driver -> driver.getTitle().equals("About"));

        //Gets the current URL
        var url = driver.getCurrentUrl();

        //Checks whether the url matches
        assertEquals("http://localhost:8080/about", url);

        //Ends the browser session
        driver.quit();
    }

    @Test
    public void addUser(){
        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        //Loads the page
        driver.get("http://localhost:8080/master-detail");

        //Have to explicitly wait because it takes time for compiled html to load
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(_driver -> !_driver.getTitle().isEmpty());

        //Test data
        var firstName = "FirstName";
        var lastName = "LastName";
        var email = "first.last@example.com";
        var phone = "(111) 111-1111";
        //Cannot use simple String because the form and table display the dob differently
        var dob = LocalDate.of(2000, Month.JANUARY, 1);
        var occupation = "Forester";

        //Adds First Name
        var firstNameTextInput = driver.findElement(By.id("vaadin-text-field-0"));
        firstNameTextInput.click();
        firstNameTextInput.sendKeys(firstName);

        //Adds Last Name
        var lastNameTextInput = driver.findElement(By.id("vaadin-text-field-1"));
        lastNameTextInput.click();
        lastNameTextInput.sendKeys(lastName);

        //Adds Email
        var emailTextInput = driver.findElement(By.id("vaadin-text-field-2"));
        emailTextInput.click();
        emailTextInput.sendKeys(email);

        //Adds Phone
        var phoneTextInput = driver.findElement(By.id("vaadin-text-field-3"));
        phoneTextInput.click();
        phoneTextInput.sendKeys(phone);

        //Adds DOB
        var dobTextInput = driver.findElement(By.id("vaadin-date-picker-4"));
        dobTextInput.click();
        dobTextInput.sendKeys(DateTimeFormatter.ofPattern("dd/MM/uuuu").format(dob));
        dobTextInput.sendKeys(Keys.ENTER); //Closes the pop-up Date Picker

        //Adds Occupation
        var occupationTextInput = driver.findElement(By.id("vaadin-text-field-5"));
        occupationTextInput.click();
        occupationTextInput.sendKeys(occupation);

        //Marks as Important
        driver.findElement(By.id("vaadin-checkbox-6"))
                .click();

        //Clicks Save
        driver.findElement(By.xpath("//vaadin-button[contains(.,'Save')]")).click();

        //Sorts by Phone number so the sample user is visible on the screen
        driver.findElement(By.xpath("//vaadin-grid-sorter[contains(.,'Phone')]")).click();

        //Reduces verbosity
        var xPathStart = "//vaadin-grid-cell-content[contains(.,'";
        var xPathEnd = "')]";

        //Waits for the page to sort
        new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(_driver -> _driver.findElement(By.xpath(xPathStart + firstName + xPathEnd)).isDisplayed());

        //Gets the cells in the table for the newly added user
        var firstNameCell = driver.findElement(By.xpath(xPathStart + firstName + xPathEnd));
        var lastNameCell = driver.findElement(By.xpath(xPathStart + lastName + xPathEnd));
        var emailCell = driver.findElement(By.xpath(xPathStart + email + xPathEnd));
        var phoneCell = driver.findElement(By.xpath(xPathStart + phone + xPathEnd));
        var dobCell = driver.findElement(By.xpath(xPathStart + dob + xPathEnd));
        var occupationCell = driver.findElement(By.xpath(xPathStart + occupation + xPathEnd));

        //Assertions
        assertEquals(firstName, firstNameCell.getText());
        assertEquals(lastName, lastNameCell.getText());
        assertEquals(email, emailCell.getText());
        assertEquals(phone, phoneCell.getText());
        assertEquals(dob.toString(), dobCell.getText());
        assertEquals(occupation, occupationCell.getText());

        //Ends the browser session
        driver.quit();
    }
}
