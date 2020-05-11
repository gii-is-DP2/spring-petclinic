package org.springframework.samples.petclinic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public LoginPage setUsername(String username) {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		
		return this;
	}
	
	public LoginPage setPassword(String password) {
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		
		return this;
	}
	
	public HomePage submit() {
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		return new HomePage(driver);
	}
}
