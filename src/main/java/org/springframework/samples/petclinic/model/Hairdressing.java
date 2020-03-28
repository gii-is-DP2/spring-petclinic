package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "hairdressings")
public class Hairdressing extends Booking{
	@NotNull(message="must not be empty")
	private TipoCuidado cuidado;
	
	@NotNull(message="must not be empty")
	private String time;
}
