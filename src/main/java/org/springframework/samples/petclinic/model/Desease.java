
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "deseases")
public class Desease extends NamedEntity {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "deseases_medicines", joinColumns = @JoinColumn(name = "desease_id"), inverseJoinColumns = @JoinColumn(name = "medicine_id"))
	private Set<Medicine>	medicines;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "deseases_diagnosis", joinColumns = @JoinColumn(name = "desease_id"), inverseJoinColumns = @JoinColumn(name = "diagnosis_id"))
	private Set<Diagnosis>	diagnosis;


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

	protected Set<Diagnosis> getDiagnosisInternal() {
		if (this.diagnosis == null) {
			this.diagnosis = new HashSet<>();
		}
		return this.diagnosis;
	}

	public void setDiagnosis(final Set<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<Diagnosis> getDiagnosis() {
		List<Diagnosis> sortedDiagnosis = new ArrayList<>(this.getDiagnosisInternal());
		PropertyComparator.sort(sortedDiagnosis, new MutableSortDefinition("date", true, true));
		return Collections.unmodifiableList(sortedDiagnosis);
	}

}
