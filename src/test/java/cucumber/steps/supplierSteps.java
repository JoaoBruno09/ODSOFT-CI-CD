package cucumber.steps;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;

import static java.time.Duration.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.time.Duration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class supplierSteps {

    WebDriver driver = null;
    
    @Given("the known credentials {string} {string}")
    public void known_the_credentials(String string, String string2) {
        if(SystemUtils.IS_OS_WINDOWS){
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }
        else{
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        driver = new ChromeDriver();
        driver.get("http://localhost:8082/login");
        //Wait until the page is ready
        driver.manage().window().maximize();
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Login | Vaadin CRM"));
    }

    @When("I login with the following username {string} and the following password {string}")
    public void i_login_with_the_following_username_and_the_following_password(String user, String pass) {
        //Login page elements
        WebElement usernameField = driver.findElement(By.cssSelector("#input-vaadin-text-field-6"));
        WebElement passwordField = driver.findElement(By.cssSelector("#input-vaadin-password-field-7"));
        WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button[role='button']"));

        //Login action
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.click();
        usernameField.sendKeys(user);
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordField.click();
        passwordField.sendKeys(pass);
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    @When("I get access into the main page")
    public void i_get_access_into_the_main_page() {
        //Contacts page
        String page_title = "Contacts | Vaadin CRM";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Contacts | Vaadin CRM"));
        Assertions.assertEquals(page_title, driver.getTitle());
    }

    @When("I click on suppliers menu")
    public void i_click_on_suppliers_menu() {
        //Contacts page elements
        WebElement suppliersButton = driver.findElement(By.cssSelector("a[href='suppliers']"));
        //Go to suppliers
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(suppliersButton));
        suppliersButton.click();
    }

    @Then("I should see the suppliers page")
    public void i_should_see_the_suppliers_page() {
        //Suppliers page
        String pageTitle = "Suppliers | Vaadin CRM";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Suppliers | Vaadin CRM"));
        Assertions.assertEquals(pageTitle, driver.getTitle());
    }

    @When("I click the add supplier button")
    public void i_click_the_add_supplier_button() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement addSuppliersButton = driver.findElement(By.cssSelector("#ROOT-2521314 > vaadin-app-layout > vaadin-vertical-layout.list-view > vaadin-horizontal-layout > vaadin-button"));
            //Go to add suppliers button
            new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(addSuppliersButton));
            addSuppliersButton.click();    
    }

    @When("I on the products combo box")
    public void i_on_the_products_combo_box() {
        WebElement multiSelect = driver.findElement(By.cssSelector("#input-vaadin-multi-select-combo-box-20"));
        multiSelect.click();

    }

    @Then("I should see multiple product categories")
    public void i_should_see_multiple_product_categories() {
        WebElement option1 = driver.findElement(By.cssSelector("#vaadin-multi-select-combo-box-item-0"));
        WebElement option2 = driver.findElement(By.cssSelector("#vaadin-multi-select-combo-box-item-1"));
        option1.getText();
        option2.getText();

    }

}

