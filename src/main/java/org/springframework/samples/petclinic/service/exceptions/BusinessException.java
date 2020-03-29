package org.springframework.samples.petclinic.service.exceptions;

import lombok.Data;

@Data
public class BusinessException extends Exception {

	private String field;
	private String code;
	
	public BusinessException(String field, String code, String message) {
		super(message);
		this.field = field;
		this.code = code;
	}
}
