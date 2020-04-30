package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Carer extends Employee {
	
	@Column(name = "isHairdresser")
	@NotNull
	private Boolean isHairdresser;

}
