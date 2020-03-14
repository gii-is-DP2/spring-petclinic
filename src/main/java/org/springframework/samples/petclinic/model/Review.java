package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Review extends NamedEntity{

	private String text;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate creation;
	
	private Services serviceType;

}
