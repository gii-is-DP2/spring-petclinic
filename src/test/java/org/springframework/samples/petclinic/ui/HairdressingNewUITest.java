package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class HairdressingNewUITest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "/home/prada/eclipse/eclipse-ee/workspace/.geckodriver/";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "geckodriver");
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testSolicitarCita() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("george");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("george");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Reservations")).click();
		driver.findElement(By.linkText("Hairdressings")).click();
		driver.findElement(By.linkText("Add hairdressing")).click();
		driver.findElement(By.id("date")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("Next")).click();
		driver.findElement(By.linkText("31")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Test");
		new Select(driver.findElement(By.id("time"))).selectByVisibleText("6:00");
		driver.findElement(By.xpath("//option[@value='6:00']")).click();
		new Select(driver.findElement(By.id("cuidado"))).selectByVisibleText("ESTETICA");
		driver.findElement(By.xpath("//option[@value='ESTETICA']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
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
