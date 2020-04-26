package org.springframework.samples.petclinic.util;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.samples.petclinic.model.ServiceType;
import lombok.Data;

@Data
public class ReviewDTO {
	
	@NotNull(message="must not be empty")
	private ServiceType serviceType;

	@NotNull(message="must not be empty")
	@Min(value=1, message="Cannot be smaller than 1")
	@Max(value=5, message="Cannot be greater than 5")
	private Integer rating;
	
	@NotEmpty(message="must not be empty")
	private String comments;
}
