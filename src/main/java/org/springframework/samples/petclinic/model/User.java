package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{
	
	//Agregar restriccion de unique
	@Id
	@NotEmpty
	String username;
	
	@NotEmpty
	String password;
	
	boolean enabled;
}
