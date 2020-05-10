package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddReviewUITest {

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
	public void testAddReview() throws Exception {
		whenLoggedInAs("fede", "fede").
		thenIAddAReview().
		thenIAmTakenToTheReviewList();	
	}
	
	private AddReviewUITest whenLoggedInAs(String username, String password) {
		driver.get(baseUrl);
		driver.findElement(By.linkText("USER")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private AddReviewUITest thenIAddAReview() {
		driver.findElement(By.linkText("REVIEWS")).click();
		driver.findElement(By.linkText("Add review")).click();
		driver.findElement(By.id("comments")).click();
		driver.findElement(By.id("comments")).clear();
		driver.findElement(By.id("comments")).sendKeys("Esto esta epico");
		new Select(driver.findElement(By.id("rating"))).selectByVisibleText("5");
		driver.findElement(By.xpath("//option[@value='5']")).click();
		new Select(driver.findElement(By.id("serviceType"))).selectByVisibleText("TRAINING");
		driver.findElement(By.xpath("//option[@value='TRAINING']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private void thenIAmTakenToTheReviewList() {
		try {
			assertEquals("Add review", driver.findElement(By.linkText("Add review")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
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
