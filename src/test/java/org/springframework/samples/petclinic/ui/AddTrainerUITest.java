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
	
	private String username;
	private Trainer trainer;

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  
		this.initTrainer();
	}

	@Test
	public void testAddTrainer() throws Exception {
		as("admin", "admin").
		whenIamLoggedIntheSystem().
		openAddTrainers().
		addTrainer().
		openTrainers().
		thenTrainerIsPresent();
	}
  
	private AddTrainerUITest whenIamLoggedIntheSystem() {	
		return this;
	}

	private AddTrainerUITest as(String username, String password) {
		this.username=username;
		
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


	private AddTrainerUITest openAddTrainers() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[4]/a/span[2]")).click();
		
		return this;
	}
	
	private AddTrainerUITest openTrainers() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
		
		return this;
	}
	
	private AddTrainerUITest addTrainer() {
		driver.findElement(By.id("firstName")).click();
	    driver.findElement(By.id("firstName")).clear();
	    driver.findElement(By.id("firstName")).sendKeys(this.trainer.getFirstName());
	    driver.findElement(By.id("lastName")).clear();
	    driver.findElement(By.id("lastName")).sendKeys(this.trainer.getLastName());
	    driver.findElement(By.id("salary")).clear();
	    driver.findElement(By.id("salary")).sendKeys(this.trainer.getSalary() + "");
	    driver.findElement(By.id("dni")).clear();
	    driver.findElement(By.id("dni")).sendKeys(this.trainer.getDni());
	    driver.findElement(By.id("telephone")).clear();
	    driver.findElement(By.id("telephone")).sendKeys(this.trainer.getTelephone());
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys(this.trainer.getEmail());
	    driver.findElement(By.id("specialty")).clear();
	    driver.findElement(By.id("specialty")).sendKeys(this.trainer.getSpecialty());
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys(this.trainer.getDescription());
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		return this;
	}

	private AddTrainerUITest thenTrainerIsPresent() {
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
	
	    return this;
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
