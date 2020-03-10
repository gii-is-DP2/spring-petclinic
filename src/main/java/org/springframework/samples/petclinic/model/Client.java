
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class Client extends Usuario {

	@Column(name = "healthInsurance")
	@NotNull
	private String	healthInsurance;

	@Column(name = "healthCardNumber")
	@NotNull
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
