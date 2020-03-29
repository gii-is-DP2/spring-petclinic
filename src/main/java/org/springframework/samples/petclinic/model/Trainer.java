package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class Trainer extends Employee{
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trainer", fetch = FetchType.EAGER)
	private Set<Training> trainings;
	
	@Column(name = "specialty")
	@NotEmpty
	private String specialty;
	
	@Column(name = "description")
	@NotEmpty
	private String description;

	@Override
	public String toString() {
		return "Trainer [specialty=" + specialty + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	
}
