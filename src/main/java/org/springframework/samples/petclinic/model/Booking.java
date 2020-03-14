package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class Booking extends BaseEntity{

	private String description;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return this.description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
