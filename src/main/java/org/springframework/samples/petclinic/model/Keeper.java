package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "keepers")
public class Keeper extends Employee {

	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "keeper")
	// private Set<Reservation> reservations;
}
