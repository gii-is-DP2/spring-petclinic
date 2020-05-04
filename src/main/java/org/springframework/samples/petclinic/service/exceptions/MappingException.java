package org.springframework.samples.petclinic.service.exceptions;

import lombok.Data;

@Data
public class MappingException extends Exception {

	
	private String entity;
	private String error;
	
	public MappingException(String entity, String error, String message) {
		super(message);
		this.entity = entity;
		this.error = error;
	}
}
