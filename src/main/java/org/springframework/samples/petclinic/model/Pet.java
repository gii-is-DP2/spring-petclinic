/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	@Column(name = "birth_date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Training> trainings;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Visit> visits;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private List<Hairdressing> hairdressings;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private List<Daycare> daycares;


	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Owner getOwner() {
		return this.owner;
	}

	protected void setOwner(Owner owner) {
		this.owner = owner;
	}

	protected Set<Visit> getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet<>();
		}
		return this.visits;
	}

	protected void setVisitsInternal(Set<Visit> visits) {
		this.visits = visits;
	}

	public List<Visit> getVisits() {
		List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(Visit visit) {
		getVisitsInternal().add(visit);
		visit.setPet(this);
	}
	
	protected Set<Training> getTrainingsInternal() {
		if (this.trainings == null) {
			this.trainings = new HashSet<>();
		}
		return this.trainings;
	}

	protected void setTrainignsInternal(Set<Training> trainings) {
		this.trainings = trainings;
	}

	public List<Training> getTrainings() {
		List<Training> sortedTrainings = new ArrayList<>(getTrainingsInternal());
		PropertyComparator.sort(sortedTrainings, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedTrainings);
	}

	public void addTraining(Training training) {
		getTrainingsInternal().add(training);
		training.setPet(this);
	}

	public void removeTraining(int trainingId) {
		Set<Training> trainings = this.getTrainingsInternal();
		for (Training t : trainings) {
			if (t.getId() == trainingId) {
				trainings.remove(t);
			}
		}
	}

	protected List<Hairdressing> getHairdressingsInternal() {
		if (this.hairdressings == null) {
			this.hairdressings = new ArrayList<>();
		}
		return this.hairdressings;
	}

	protected void setHairdressingsInternal(List<Hairdressing> hairdressings) {
		this.hairdressings = hairdressings;
	}

	public List<Hairdressing> getHairdressings() {
		List<Hairdressing> sortedHairdressings = new ArrayList<>(getHairdressingsInternal());
		PropertyComparator.sort(sortedHairdressings, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedHairdressings);	
	}

	public void addHairdressing(Hairdressing hairdressing) {
		getHairdressingsInternal().add(hairdressing);
		hairdressing.setPet(this);
	}

	public void deleteHairdressing(Integer id) {	
		List<Hairdressing> hairdressings = this.getHairdressingsInternal();
		Hairdressing aux = new Hairdressing();
		for (Hairdressing h  : hairdressings) {
			if (h.getId() == id) {
				aux = h;
				break;
			}
		}
		hairdressings.remove(aux);
	}
		
	protected List<Daycare> getDaycaresInternal() {
		if (this.daycares == null) {
			this.daycares = new ArrayList<Daycare>();
		}
		return this.daycares;
	}
	
	protected void setDaycaresInternal(final List<Daycare> daycares) {
		this.daycares = daycares;
	}
	
	public List<Daycare> getDaycares() {
		List<Daycare> sortedDaycares = new ArrayList<>(this.getDaycaresInternal());
		PropertyComparator.sort(sortedDaycares, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedDaycares);
	}
	
	public void addDaycare(Daycare daycare) {
		getDaycaresInternal().add(daycare);
		daycare.setPet(this);
	}

	public void deleteDaycare(int daycareId) {
		List<Daycare> daycares = this.getDaycaresInternal();
		Daycare aux = new Daycare();
		for (Daycare d : daycares) {
			if (d.getId() == daycareId) {
				aux=d;
				break;
			}
		}
	}
}
