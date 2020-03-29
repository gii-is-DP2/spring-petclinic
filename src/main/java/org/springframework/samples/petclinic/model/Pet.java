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

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
	private Set<Visit> visits;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
	private List<Hairdressing> hairdressings;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
	private Set<Daycare> daycares;


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
		System.out.println("sortedHairdressings preordeanmiento: \n\n\n\n\n"+sortedHairdressings);
		PropertyComparator.sort(sortedHairdressings, new MutableSortDefinition("date", false, false));
		System.out.println("sortedHairdressings postordeanmiento: \n\n\n\n\n"+sortedHairdressings);
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
			}
		}
		hairdressings.remove(aux);
		

	protected Set<Daycare> getDaycaresInternal() {
		if (this.daycares == null) {
			this.daycares = new HashSet<>();
		}
		return this.daycares;
	}
	
	protected void setDaycaresInternal(final Set<Daycare> daycares) {
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
		Set<Daycare> daycares = this.getDaycaresInternal();
		for (Daycare d : daycares) {
			if (d.getId() == daycareId) {
				daycares.remove(d);
			}
		}

	}

}
