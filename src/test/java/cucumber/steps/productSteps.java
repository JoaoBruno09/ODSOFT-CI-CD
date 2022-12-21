package cucumber.steps;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class productSteps {
    
    WebDriver driver = null;
    
    @Given("known credentials {string} {string}")
    public void known_credentials(String string, String string2) {
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

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String user, String pass) {
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

    @When("I get access to the mainpage")
    public void i_get_access_to_the_mainpage() {
        //Contacts page
        String page_title = "Contacts | Vaadin CRM";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Contacts | Vaadin CRM"));
        Assertions.assertEquals(page_title, driver.getTitle());

    }

    @When("I click on product category menu")
    public void i_click_on_product_category_menu() {
        //Contacts page elements
        WebElement productsButton = driver.findElement(By.cssSelector("a[href='product']"));
        //Go to products
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(productsButton));
        productsButton.click();
    }

    @Then("we should see the product category page")
    public void we_should_see_the_product_category_page() {
        //Products page
        String pageTitle = "Product Categories | Vaadin CRM";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Product Categories | Vaadin CRM"));
        Assertions.assertEquals(pageTitle, driver.getTitle());

    }

    @When("I can't see product category menu")
    public void i_can_t_see_product_category_menu() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();

    }

    @When("I try to navigate via URL")
    public void i_try_to_navigate_via_url() {
        driver.get("http://localhost:8082/product");
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }

    @Then("I should see a page that I could not navigate")
    public void i_should_see_a_page_that_i_could_not_navigate() {
        
        String errorMessage = "Could not navigate to 'product'";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement cannotRedirect = driver.findElement(By.cssSelector("#ROOT-2521314 > div > div"));

        Assertions.assertEquals(errorMessage, cannotRedirect.getText());
    }
}

