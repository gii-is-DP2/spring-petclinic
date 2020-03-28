package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "training")
public class Training extends Booking{
	
//	@ManyToOne(optional = false)
//	@JoinColumn(name = "pet_id")
//	private Pet pet;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "trainer_id")
	private Trainer trainer;
	
	@Column(name="tipoPista")
	@NotNull(message="must not be empty")
	private TipoPista tipoPista;
	
	@Column(name="pista")
	@NotNull(message="must not be empty")
	private Integer pista;
}
