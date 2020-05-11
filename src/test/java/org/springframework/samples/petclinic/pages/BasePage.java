package org.springframework.samples.petclinic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasePage {
	
	protected final WebDriver driver;
	
	public BasePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public AddTrainerPage goToAddTrainer() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[4]/a/span[2]")).click();
	    
	    return new AddTrainerPage(driver);
	}
	
	public TrainersPage goToTrainers() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
	    
	    return new TrainersPage(driver);
	}
	
	public LoginPage goToLogin() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/ul/li[1]/a")).click();
		
		return new LoginPage(driver);
	}
	
}
