
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class Professional extends Usuario {

	@Column(name = "specialty")
	@NotEmpty(message = "*")
	private String	specialty;

	@Column(name = "collegiateNumber")
	@NotEmpty(message = "*")
	private Integer	collegiateNumber;


	public String getSpecialty() {
		return this.specialty;
	}

	public void setSpecialty(final String specialty) {
		this.specialty = specialty;
	}

	public Integer getCollegiateNumber() {
		return this.collegiateNumber;
	}

	public void setCollegiateNumber(final Integer collegiateNumber) {
		this.collegiateNumber = collegiateNumber;
	}
}
