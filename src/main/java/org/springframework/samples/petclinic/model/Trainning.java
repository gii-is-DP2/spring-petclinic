package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "trainning")
public class Trainning extends Booking{
	private TipoPista pista;
	private Integer tipoPista;
}
