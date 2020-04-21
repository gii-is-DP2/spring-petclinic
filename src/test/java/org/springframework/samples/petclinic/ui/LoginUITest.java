package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
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
		thenISeeTrainersInTheMenuBar();	
	}

	private LoginUITest thenISeeMyUsernameInTheMenuBar() {
		assertEquals(username.toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[6]/a/strong")).getText());
		return this;
	}
	
	private LoginUITest thenISeeTrainersInTheMenuBar() {
		assertEquals("Trainers".toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a")).getText());
		return this;
	}

	private LoginUITest whenIamLoggedIntheSystem() {	
		return this;
	}

	
	private LoginUITest as(String username, String password) {
		this.username=username;
		System.out.println(port);
		driver.get(this.baseUrl);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/ul/li[1]/a")).click();
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