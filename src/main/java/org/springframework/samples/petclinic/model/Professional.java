
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Professional extends Usuario {

	@Column(name = "specialty")
	@NotEmpty
	private String	specialty;

	@Column(name = "collegiateNumber")
	@NotNull
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
