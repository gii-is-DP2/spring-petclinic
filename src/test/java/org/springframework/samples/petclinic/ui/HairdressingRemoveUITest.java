package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.*;

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
public class HairdressingRemoveUITest {

	@LocalServerPort
	private int port;

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
	public void testRemoveHairdressing() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("USER")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("george");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("george");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("RESERVATIONS")).click();
		driver.findElement(By.linkText("HAIRDRESSINGS")).click();
		driver.findElement(By.linkText("2021-01-01")).click();
		driver.findElement(By.linkText("Cancel hairdressing")).click();
		// Warning: waitForTextNotPresent may require manual changes
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if (!driver.findElement(By.cssSelector("BODY")).getText()
						.matches("^[\\s\\S]*link=2020-01-01[\\s\\S]*$"))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}

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