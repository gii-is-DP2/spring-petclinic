package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginUITest {
	@LocalServerPort
	private int port;

	private String username;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testLoginAsAdmin() throws Exception {
		as("admin", "admin").
		whenIamLoggedIntheSystem().
		thenISeeMyUsernameInTheMenuBar().
		thenISeeEmployeesInTheMenuBar();	
	}

	private LoginUITest thenISeeMyUsernameInTheMenuBar() {
		assertEquals(username.toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[6]/a/strong")).getText());
		return this;
	}
	
	private LoginUITest thenISeeEmployeesInTheMenuBar() {
		assertEquals("Employees".toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).getText());
		return this;
	}

	private LoginUITest whenIamLoggedIntheSystem() {	
		return this;
	}

	
	private LoginUITest as(String username, String password) {
		this.username=username;
		System.out.println(port);
		driver.get(this.baseUrl);
		driver.findElement(By.linkText("USER")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	    	fail(verificationErrorString);
	    }
	}

	private boolean isElementPresent(By by) {
		try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	    	return false;
	    }	
	}
	
	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
    	} catch (NoAlertPresentException e) {
    		return false;
	    }
	}
	
	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
		    String alertText = alert.getText();
		    if (acceptNextAlert) {
		    	alert.accept();
		    } else {
		    	alert.dismiss();
		    }
		    return alertText;
	    } finally {
	    	acceptNextAlert = true;
	    }
	}
}