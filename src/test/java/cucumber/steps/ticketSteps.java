package cucumber.steps;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;

import static java.time.Duration.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ticketSteps {

    WebDriver driver = null;
    
    @Given("The url for the customer form")
    public void the_url_for_the_customer_form() {
        if(SystemUtils.IS_OS_WINDOWS){
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }
        else{
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        driver = new ChromeDriver();
        driver.get("http://localhost:8082/customer");
    }

    @When("I get access to the customer form")
    public void i_get_access_to_the_customer_form() {
        String page_url = "Customer | Vaadin CRM";
        driver.manage().window().maximize();
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Customer | Vaadin CRM"));
        Assertions.assertEquals(page_url, driver.getTitle());
    }

    @When("I submit the form with the name {string} and the adress {string}")
    public void i_submit_the_form_with_the_name_and_the_adress(String name, String address) {
        //customer page
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Customer | Vaadin CRM"));
        //Name field
        WebElement customerAddressField = driver.findElement(By.cssSelector("#input-vaadin-text-field-6"));
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(customerAddressField));
        customerAddressField.click();
        customerAddressField.sendKeys(address);
        //Address Field
        WebElement customerNameField = driver.findElement(By.cssSelector("#input-vaadin-text-field-7"));
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(customerAddressField));
        customerNameField.click();
        customerNameField.sendKeys(name);
        customerNameField.sendKeys(Keys.ENTER);
    }

    @When("I get access to the tickets page")
    public void i_get_access_to_the_tickets_page() {
        String page_url = "Tickets | Vaadin CRM";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Tickets | Vaadin CRM"));

        Assertions.assertEquals(page_url, driver.getTitle());
    }

    @Then("I should see only the tickets i submitted")
    public void i_should_see_only_the_tickets_i_submitted() {
        String ticket1Description = "zun capfad";

        String xPathStart = "//vaadin-grid-cell-content[contains(.,'";
        String xPathEnd = "')]";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                .until(visibilityOfElementLocated(By.xpath(xPathStart + ticket1Description + xPathEnd)));

        //Gets the cells in the table for the newly added user
        WebElement lastDescriptionCell = driver.findElement(By.xpath(xPathStart + ticket1Description + xPathEnd));

        Assertions.assertEquals(ticket1Description, lastDescriptionCell.getText());
    }

}

