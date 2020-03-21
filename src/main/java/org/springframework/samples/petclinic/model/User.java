package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{
	
	//Agregar restriccion de unique
	@Id
	String username;
	
	String password;
	
	boolean enabled;
}
