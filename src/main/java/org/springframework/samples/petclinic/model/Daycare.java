package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "daycare")
public class Daycare extends Booking{
	
	private Integer aforo;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
}
