package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Review extends BaseEntity{

	private String comments;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate date;
	
	private ServiceType serviceType;

    @Min(value=1, message="must be equal or greater than 1")
    @Max(value=5, message="must be equal or less than 5")  
	private int rating;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private Owner owner;
}