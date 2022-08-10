import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class SimpleTestIT {

    @Test
    public void checkPageLoad() {
        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        try {
            //Loads the page
            driver.get("http://localhost:8080/hello");

            //Have to explicitly wait because it takes time for compiled html to load
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                    .until(titleIs("Hello World"));

            //Get the Say Hello button
            var button = driver.findElement(By.xpath("//vaadin-button[contains(.,'Say hello')]"));

            //Check the content of the button
            assertEquals(button.getText(), "Say hello");
        } finally {
            //Ends the browser session
            driver.quit();
        }
    }

    @Test
    public void routeSwitch(){
        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        try {
            //Loads the page
            driver.get("http://localhost:8080");

            //Have to explicitly wait because it takes time for compiled html to load
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                    .until(titleIs("Hello World"));

            //Clicks on the About button
            driver.findElement(By.cssSelector("vcf-nav-item:nth-child(2)"))
                    .click();

            //Wait for new page to load
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                    .until(titleIs("About"));

            //Gets the current URL
            var url = driver.getCurrentUrl();

            //Checks whether the url matches
            assertEquals("http://localhost:8080/about", url);
        } finally {
            //Ends the browser session
            driver.quit();
        }
    }

    @Test
    public void addUser(){
        //Set up the web driver
        WebDriverManager.chromedriver().setup();

        //Use this ChromeDriver to interact with Chrome
        var driver = new ChromeDriver();

        try {
            //Maximizes the screen
            driver.manage().window().maximize();

            //Loads the page
            driver.get("http://localhost:8080/master-detail");

            //Have to explicitly wait because it takes time for compiled html to load
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                    .until(titleIs("Master-Detail"));

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
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                    .until(visibilityOfElementLocated(By.xpath(xPathStart + firstName + xPathEnd)));

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
        } finally {
            //Ends the browser session
            driver.quit();
        }
    }
}