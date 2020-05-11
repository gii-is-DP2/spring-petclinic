package org.springframework.samples.petclinic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.samples.petclinic.model.Trainer;

public class AddTrainerPage extends BasePage {

	public AddTrainerPage(WebDriver driver) {
		super(driver);
	}
	
	public AddTrainerPage setTrainer(Trainer trainer) {
		driver.findElement(By.id("firstName")).click();
	    driver.findElement(By.id("firstName")).clear();
	    driver.findElement(By.id("firstName")).sendKeys(trainer.getFirstName());
	    driver.findElement(By.id("lastName")).clear();
	    driver.findElement(By.id("lastName")).sendKeys(trainer.getLastName());
	    driver.findElement(By.id("salary")).clear();
	    driver.findElement(By.id("salary")).sendKeys(trainer.getSalary() + "");
	    driver.findElement(By.id("dni")).clear();
	    driver.findElement(By.id("dni")).sendKeys(trainer.getDni());
	    driver.findElement(By.id("telephone")).clear();
	    driver.findElement(By.id("telephone")).sendKeys(trainer.getTelephone());
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys(trainer.getEmail());
	    driver.findElement(By.id("specialty")).clear();
	    driver.findElement(By.id("specialty")).sendKeys(trainer.getSpecialty());
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys(trainer.getDescription());
	    
	    return this;
	}
	
	public TrainerDetailPage submit() {
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		return new TrainerDetailPage(driver);
	}

}
