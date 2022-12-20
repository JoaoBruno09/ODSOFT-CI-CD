package endtoend;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;


public class ProductE2E {

    @Test
    public void createProduct() throws InterruptedException {
        if(SystemUtils.IS_OS_WINDOWS){
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }
        else{
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8082/login");
        //Wait until the page is ready
        driver.manage().window().maximize();
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Login | Vaadin CRM"));

        //Login page elements
        WebElement usernameField = driver.findElement(By.cssSelector("#input-vaadin-text-field-6"));
        WebElement passwordField = driver.findElement(By.cssSelector("#input-vaadin-password-field-7"));
        WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button[role='button']"));

        //Login action
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.click();
        usernameField.sendKeys("admin");
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordField.click();
        passwordField.sendKeys("userpass");
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();

        //Contacts page
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Contacts | Vaadin CRM"));

        //Contacts page elements
        WebElement productsButton = driver.findElement(By.cssSelector("a[href='product']"));

        //Go to products
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(productsButton));
        productsButton.click();

        //Products page
        String dummyProductCategory = "Soda";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Product Categories | Vaadin CRM"));

        //Products categories page elements
        WebElement addProd = driver.findElement(By.cssSelector("vaadin-horizontal-layout[class='toolbar'] vaadin-button[role='button']"));

        //Open form
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(addProd));
        addProd.click();

        WebElement prodForm = driver.findElement(By.cssSelector("#ROOT-2521314 > vaadin-app-layout > vaadin-vertical-layout.list-view > div > vaadin-form-layout"));

        new WebDriverWait(driver, ofSeconds(10)).until(visibilityOf(prodForm));

        WebElement textfield = driver.findElement(By.cssSelector("#input-vaadin-text-field-11"));
        textfield.click();
        textfield.sendKeys(dummyProductCategory);
        textfield.sendKeys(Keys.ENTER);

        String xPathStart = "//vaadin-grid-cell-content[contains(.,'";
        String xPathEnd = "')]";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                .until(visibilityOfElementLocated(By.xpath(xPathStart + dummyProductCategory + xPathEnd)));

        //Gets the cells in the table for the newly added product
        WebElement lastDescriptionCell = driver.findElement(By.xpath(xPathStart + dummyProductCategory + xPathEnd));

        Assertions.assertEquals(dummyProductCategory, lastDescriptionCell.getText());
    }
}
