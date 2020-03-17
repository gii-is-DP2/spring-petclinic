package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class Trainer extends Employee{
	
	@Column(name = "specialty")
	@NotEmpty
	private String specialty;
	
	@Column(name = "description")
	@NotEmpty
	private String description;
}
