
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class Client extends Usuario {

	@Column(name = "healthInsurance")
	@NotEmpty(message = "*")
	private String	healthInsurance;

	@Column(name = "healthCardNumber")
	@NotEmpty(message = "*")
	private String	healthCardNumber;


	public String getHealthInsurance() {
		return this.healthInsurance;
	}

	public void setHealthInsurance(final String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public String getHealthCardNumber() {
		return this.healthCardNumber;
	}

	public void setHealthCardNumber(final String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}
}
