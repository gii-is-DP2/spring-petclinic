package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteDaycareUITest {

	@LocalServerPort
	private int port;

	@Autowired
	PetService petService;

	private String user;
	private Daycare daycare;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private int rowsBefore = 0;

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testDeleteDaycareUI() throws Exception {
		initDaycare();
		whenLoggedInAs("george", "george")
		.thenOpenMyDaycares()
		.thenDeleteDaycare()
		.thenDaycareIsDeleted();
	}
	
	private DeleteDaycareUITest whenLoggedInAs(String username, String password) {
		driver.get(baseUrl);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	private DeleteDaycareUITest thenOpenMyDaycares() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[3]/a")).click();
		rowsBefore = driver.findElements(By.xpath("//table[@id='daycaresTable']/tbody/tr")).size();
		return this;
	}
	
	private DeleteDaycareUITest thenDeleteDaycare() {
		driver.findElement(By.linkText("2022-02-02")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Cancel daycare')]")).click();
		return this;
	}

	private void initDaycare() {
		this.user = "george";
		List<Pet> pets = this.petService.findPetsByOwner(this.user);

		this.daycare = new Daycare();
		
		this.daycare.setDate(LocalDate.of(2022, 02, 02));
		this.daycare.setDescription("Descripcion de prueba");
		this.daycare.setCapacity(10);
		this.daycare.setPet(pets.get(0));
	}

	private DeleteDaycareUITest thenDaycareIsDeleted() {
		int rowsAfter = driver.findElements(By.xpath("//table[@id='daycaresTable']/tbody/tr")).size();
		try {
			assertEquals(rowsBefore - 1, rowsAfter);
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
