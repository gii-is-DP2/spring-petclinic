package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
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
public class AddReviewRepeatedServiceUITest {

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
	public void testAddRepeatedReviewUI() throws Exception {
		whenLoggedInAs("fede", "fede").
		thenIAddAReview().
		thenIAmTakenToTheReviewList().
		thenIAddAnotherReviewOfSameService().
		thenIGetAnErrorMessage();
	}
	
	private AddReviewRepeatedServiceUITest whenLoggedInAs(String username, String password) {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("USER")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("fede");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("fede");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private AddReviewRepeatedServiceUITest thenIAddAReview() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
		driver.findElement(By.linkText("Add review")).click();
		new Select(driver.findElement(By.id("serviceType"))).selectByVisibleText("TRAINING");
		driver.findElement(By.xpath("//option[@value='TRAINING']")).click();
		new Select(driver.findElement(By.id("rating"))).selectByVisibleText("4");
		driver.findElement(By.xpath("//option[@value='4']")).click();
		driver.findElement(By.id("comments")).click();
		driver.findElement(By.id("comments")).clear();
		driver.findElement(By.id("comments")).sendKeys("Mi mascota quedo contenta");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private AddReviewRepeatedServiceUITest thenIAmTakenToTheReviewList() {
		try {
			assertEquals("Add review", driver.findElement(By.linkText("Add review")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		return this;
	}
	
	private AddReviewRepeatedServiceUITest thenIAddAnotherReviewOfSameService() {
		driver.findElement(By.linkText("Add review")).click();
		driver.findElement(By.id("comments")).click();
		driver.findElement(By.id("comments")).clear();
		driver.findElement(By.id("comments")).sendKeys("Mi mascota quedo contenta otra vez");
		new Select(driver.findElement(By.id("rating"))).selectByVisibleText("5");
		driver.findElement(By.xpath("//option[@value='5']")).click();
		new Select(driver.findElement(By.id("serviceType"))).selectByVisibleText("TRAINING");
		driver.findElement(By.xpath("//option[@value='TRAINING']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private AddReviewRepeatedServiceUITest thenIGetAnErrorMessage() {
		try {
			assertEquals("You have already submitted a review for this service.",
					driver.findElement(By.xpath("//form[@id='add-review-form']/div/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
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
