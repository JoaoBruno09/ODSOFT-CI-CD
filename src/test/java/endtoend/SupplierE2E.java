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


public class SupplierE2E {

    @Test
    public void createSupplier() throws InterruptedException {

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
        WebElement suppliersButton = driver.findElement(By.cssSelector("a[href='suppliers']"));

        //Go to suppliers
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(suppliersButton));
        suppliersButton.click();

        //Suppliers page
        String dummySupplierName = "Nasa";
        String dummySupplierAddress = "Somewhere up there";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(5))
                .until(titleIs("Suppliers | Vaadin CRM"));

        //Suppliers page elements
        WebElement addSupplier = driver.findElement(By.cssSelector("#ROOT-2521314 > vaadin-app-layout > vaadin-vertical-layout.list-view > vaadin-horizontal-layout > vaadin-button"));

        //Open form
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(addSupplier));
        addSupplier.click();

        WebElement supplierForm = driver.findElement(By.cssSelector("#ROOT-2521314 > vaadin-app-layout > vaadin-vertical-layout.list-view > div > vaadin-form-layout"));

        new WebDriverWait(driver, ofSeconds(10)).until(visibilityOf(supplierForm));

        //Populating input fields
        WebElement textfieldName = driver.findElement(By.cssSelector("#input-vaadin-text-field-11"));
        textfieldName.click();
        textfieldName.sendKeys(dummySupplierName);

        WebElement textfieldAddress = driver.findElement(By.cssSelector("#input-vaadin-text-field-15"));
        textfieldAddress.click();
        textfieldAddress.sendKeys(dummySupplierAddress);


        WebElement multiSelect = driver.findElement(By.cssSelector("#input-vaadin-multi-select-combo-box-20"));
        multiSelect.click();

        WebElement option1 = driver.findElement(By.cssSelector("#vaadin-multi-select-combo-box-item-0"));
        WebElement option2 = driver.findElement(By.cssSelector("#vaadin-multi-select-combo-box-item-1"));
        option1.click();
        option2.click();

        multiSelect.sendKeys(Keys.ESCAPE);
        multiSelect.sendKeys(Keys.ENTER);

        String xPathStart = "//vaadin-grid-cell-content[contains(.,'";
        String xPathEnd = "')]";
        new WebDriverWait(driver, ofSeconds(30), ofSeconds(1))
                .until(visibilityOfElementLocated(By.xpath(xPathStart + dummySupplierName + xPathEnd)));

        //Gets the cells in the table for the newly added supplier
        WebElement lastNameCell = driver.findElement(By.xpath(xPathStart + dummySupplierName + xPathEnd));
        System.out.println(lastNameCell.getText());
        Assertions.assertEquals(dummySupplierName, lastNameCell.getText());
    }
}
