package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class Booking extends BaseEntity{

	@NotEmpty(message="must not be empty")
	@NotNull(message="must not be empty")
	private String description;

	@NotNull(message="must not be empty")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Pet getPet() {
		return this.pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}
}
