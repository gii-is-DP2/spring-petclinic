
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "diagnosis")
public class Diagnosis extends BaseEntity {

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate		date;

	@Column(name = "description")
	@NotEmpty
	private String			description;

	//Relations
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "appointment_id")
	private Appointment		appointment;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "diagnosis_medicines", joinColumns = @JoinColumn(name = "diagnosis_id"), inverseJoinColumns = @JoinColumn(name = "medicine_id"))
	private Set<Medicine>	medicines;

	//	@ManyToMany(fetch = FetchType.EAGER)
	//	@JoinTable(name = "desease_id")
	//	private Set<Desease>	deseases;


	protected Set<Medicine> getMedicinesInternal() {
		if (this.medicines == null) {
			this.medicines = new HashSet<>();
		}
		return this.medicines;
	}

	public void setMedicines(final Set<Medicine> medicines) {
		this.medicines = medicines;
	}

	public List<Medicine> getMedicines() {
		List<Medicine> sortedMedicines = new ArrayList<>(this.getMedicinesInternal());
		PropertyComparator.sort(sortedMedicines, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedMedicines);
	}

	//	protected Set<Desease> getDeseasesInternal() {
	//		if (this.deseases == null) {
	//			this.deseases = new HashSet<>();
	//		}
	//		return this.deseases;
	//	}
	//
	//	public void setDeseases(final Set<Desease> deseases) {
	//		this.deseases = deseases;
	//	}
	//
	//	public List<Desease> getDeseases() {
	//		List<Desease> sortedDeseases = new ArrayList<>(this.getDeseasesInternal());
	//		PropertyComparator.sort(sortedDeseases, new MutableSortDefinition("name", true, true));
	//		return Collections.unmodifiableList(sortedDeseases);
	//	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Appointment getAppointment() {
		return this.appointment;
	}

	public void setAppointment(final Appointment appointment) {
		this.appointment = appointment;
	}

}
