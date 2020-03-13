package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class Trainer extends Employee{
	
	private String specialty;
	private String description;

}
