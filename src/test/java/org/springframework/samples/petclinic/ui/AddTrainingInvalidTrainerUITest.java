package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddTrainingInvalidTrainerUITest {
	@Autowired
	TrainerService trainerService;
	
	@Autowired
	PetService petService;
	
	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private String username;
	private Training training;

	@BeforeEach
	public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    baseUrl = "http://localhost:" + port;
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);    
	}

	@Test
	public void testAddTraining() throws Exception {
	    as("fede", "fede").
	    whenIamLoggedIntheSystem().
	    openMyTrainings().
	    addTraining().
	    thenErrorIsShown();
	}
  
  	private AddTrainingInvalidTrainerUITest whenIamLoggedIntheSystem() {	
		return this;
	}
  
  	private AddTrainingInvalidTrainerUITest as(String username, String password) {
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
  	
  	private AddTrainingInvalidTrainerUITest openMyTrainings() {
  		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
  		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
	    
  		return this;
  	}
  
  	private AddTrainingInvalidTrainerUITest addTraining() {
  		this.initTraining();
  		
  		driver.findElement(By.linkText("Add training")).click();
  		driver.findElement(By.id("date")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
  	    driver.findElement(By.linkText("3")).click();
  	    driver.findElement(By.id("description")).click();
  	    driver.findElement(By.id("description")).clear();
  	    driver.findElement(By.id("description")).sendKeys(this.training.getDescription());
  	    driver.findElement(By.id("ground")).click();
  	    driver.findElement(By.id("ground")).clear();
  	    driver.findElement(By.id("ground")).sendKeys(this.training.getGround().toString());
  	    new Select(driver.findElement(By.id("groundType"))).selectByVisibleText(this.training.getGroundType().name().toUpperCase());
  	    driver.findElement(By.xpath("//option[@value='" + this.training.getGroundType().name().toUpperCase() + "']")).click();
  	    driver.findElement(By.id("petName")).click();
  	    driver.findElement(By.id("petName")).click();
  	    new Select(driver.findElement(By.id("trainerId"))).selectByVisibleText(this.training.getTrainer().getLastName());
  	    driver.findElement(By.xpath("//option[@value='" + this.training.getTrainer().getId() + "']")).click();
  	    driver.findElement(By.xpath("//button[@type='submit']")).click();
  	    
	  	return this;
  	}
  
  	private AddTrainingInvalidTrainerUITest thenErrorIsShown() {
  		assertTrue(isElementPresent(By.xpath("//form[@id='add-training-form']/div/div[4]/div[3]/div/span[2]")));
  	    
  	    return this;
  	}
  	
  	private void initTraining() {
  		List<Pet> pets = this.petService.findPetsByOwner(this.username);
  	    List<Trainer> trainers = (List<Trainer>)this.trainerService.findTrainers();
  	    
  	    this.training = new Training();
  	    this.training.setDescription("Fede open training");
  	    this.training.setGround(4);
  	    this.training.setGroundType(GroundType.OBEDIENCIA);
  	    this.training.setPet(pets.get(0));
  	    this.training.setTrainer(trainers.get(0));
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

