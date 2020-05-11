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

public class HairdressingUpdateUITest {
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
	public void testActualizarCita() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("USER")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("george");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("george");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("RESERVATIONS")).click();
		driver.findElement(By.linkText("HAIRDRESSINGS")).click();
		driver.findElement(By.linkText("2021-01-01")).click();
		driver.findElement(By.linkText("Edit hairdressing")).click();
		new Select(driver.findElement(By.id("time"))).selectByVisibleText("9:30");
		driver.findElement(By.xpath("//option[@value='9:30']")).click();
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
