package org.springframework.samples.petclinic.util;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.TipoCuidado;

import lombok.Data;

@Data
public class HairdressingDTO {
	
	@NotNull(message="must not be empty")
	private String petName;
	
	@NotEmpty(message="must not be empty")
	private String description;
	
	@NotEmpty(message="must not be empty")
	private String time;
	
	@NotNull(message="must not be empty")
	private TipoCuidado cuidado;

	@NotNull(message="must not be empty")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;
}
