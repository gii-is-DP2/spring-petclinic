package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.pages.BasePage;
import org.springframework.samples.petclinic.pages.HomePage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddTrainerUITest {
	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private HomePage homePage;
	
	private String username;
	private Trainer trainer;

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  
		this.homePage = new HomePage(driver);
		driver.get(this.baseUrl);
	}

	@Test
	public void testAddTrainer() throws Exception {
		this.initTrainer();
		
		this.homePage
			.goToLogin()
			.setUsername("admin")
			.setPassword("admin")
			.submit()
			.goToAddTrainer()
			.setTrainer(this.trainer)
			.submit()
			.goToTrainers();
		
		this.assertTrainerIsPresent();
	}
	
	private void assertTrainerIsPresent() {
		driver.findElement(By.name("lastName")).click();
	    driver.findElement(By.name("lastName")).clear();
		driver.findElement(By.name("lastName")).sendKeys(this.trainer.getLastName());
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	    
	    try {
	    	assertEquals(this.trainer.getFirstName() + " " + this.trainer.getLastName(), 
	  	    		  driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[1]")).getText());
	  	} catch (Error e) {
	  		verificationErrors.append(e.toString());
	  	}
	    try {
	    	assertEquals(this.trainer.getSalary() + "", driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[2]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
	  	}
	    try {
	    	assertEquals(this.trainer.getDni(), driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[3]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
  	    }
	    try {
	    	assertEquals(this.trainer.getTelephone().toString().toUpperCase(), driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[4]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
  	    }
	    try {
	    	assertEquals(this.trainer.getEmail(), driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[5]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
  	    }
	    try {
	    	assertEquals(this.trainer.getSpecialty(), driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[6]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
  	    }
	    try {
	    	assertEquals(this.trainer.getDescription(), driver.findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td[7]")).getText());
  	    } catch (Error e) {
  	    	verificationErrors.append(e.toString());
  	    }
	}

	@Test
	public void testAddInvalidTrainer() throws Exception {		
		this.homePage
			.goToLogin()
			.setUsername("admin")
			.setPassword("admin")
			.submit()
			.goToAddTrainer()
			.submit();
		
		this.assertTrainerErrorsMessageArePresent();
	}
	
	private void assertTrainerErrorsMessageArePresent() {
		assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div/div/span[2]")));
		assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div/div/span[2]")).getText());
		assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[2]/div/span[2]")));
		assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[2]/div/span[2]")).getText());
    	assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[4]/div/span[2]")));
    	assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[4]/div/span[2]")).getText());
    	assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[5]/div/span[2]")));
    	assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[5]/div/span[2]")).getText());
    	assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[6]/div/span[2]")));
    	assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[6]/div/span[2]")).getText());
    	assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[7]/div/span[2]")));
    	assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[7]/div/span[2]")).getText());
    	assertTrue(isElementPresent(By.xpath("//form[@id='add-trainer-form']/div/div[8]/div/span[2]")));
    	assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-trainer-form']/div/div[8]/div/span[2]")).getText());
	}
	
	private void initTrainer() {
		this.trainer = new Trainer();
		this.trainer.setFirstName("Pedro");
		this.trainer.setLastName("Rodriguez");
		this.trainer.setDescription("Es puntual y amigable.");
		this.trainer.setDni("47842798");
		this.trainer.setEmail("pedro@pedro.com");
		this.trainer.setSalary(45);
		this.trainer.setTelephone("625096668");
		this.trainer.setSpecialty("Adiestramiento");
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
