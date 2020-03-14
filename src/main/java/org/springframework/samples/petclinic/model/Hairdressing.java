package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "hairdressing")
public class Hairdressing extends Booking{
	private TipoCuidado cuidado;
}
