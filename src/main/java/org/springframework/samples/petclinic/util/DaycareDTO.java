package org.springframework.samples.petclinic.util;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DaycareDTO {
	
	@NotNull(message="must not be empty")
	private String petName;
	
//	@NotNull(message="must not be empty")
	private Integer capacity;
	
	@NotEmpty(message="must not be empty")
	private String description;

	@NotNull(message="must not be empty")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;
}
