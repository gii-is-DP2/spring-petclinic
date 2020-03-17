package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "training")
public class Training extends Booking{
	private TipoPista tipoPista;
	private Integer pista;
}
