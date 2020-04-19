package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "daycares")
public class Daycare extends Booking{
	
	@Column(name="capacity")
	private Integer capacity;
}
