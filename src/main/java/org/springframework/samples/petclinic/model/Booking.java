package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class Booking extends BaseEntity{

	private String description;

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
