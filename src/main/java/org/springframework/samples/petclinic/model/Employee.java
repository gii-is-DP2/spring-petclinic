package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class Employee extends Person {

	@Column(name = "salary")
	@Min(value = 0, message = "Salary must be a positive number")
	@NotNull(message = "Must not be empty")
	private double salary;

	@Column(name = "dni")
	@NotEmpty
	private String dni;

	@Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;
	
	@Column(name = "email")
	@Email
	@NotEmpty
	private String email;
	
	public double getSalary() {
		return this.salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
