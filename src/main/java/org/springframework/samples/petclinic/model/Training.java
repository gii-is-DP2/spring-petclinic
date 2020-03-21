package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "training")
public class Training extends Booking{
	
	@Column(name="tipoPista")
	@NotNull(message="must not be empty")
	private TipoPista tipoPista;
	
	@Column(name="pista")
	@NotNull(message="must not be empty")
	private Integer pista;
}
