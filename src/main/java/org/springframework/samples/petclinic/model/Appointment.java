/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple JavaBean domain object representing a Appointment.
 *
 * @author Ken Krebs
 */
@Entity
@Table(name = "appointment")
public class Appointment extends BaseEntity {

	/**
	 * Holds value of property date.
	 */
	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate		Date;

	@Column(name = "start_time")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime		startTime;

	@Column(name = "end_time")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime		endTime;

	/**
	 * Holds value of property reason.
	 */
	@NotEmpty
	@Column(name = "reason")
	private String			reason;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private AppointmentType	type;


	/**
	 * Holds value of property pet.
	 */

	/**
	 * Creates a new instance of Appointment for the current date
	 */
	public Appointment() {
		this.Date = LocalDate.now();
	}

	/**
	 * Getter for property date.
	 *
	 * @return Value of property date.
	 */
	public LocalTime getStartTime() {
		return this.startTime;
	}

	/**
	 * Setter for property date.
	 *
	 * @param date
	 *            New value of property date.
	 */
	public void setStartTime(final LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(final LocalTime endTime) {
		this.endTime = endTime;
	}
	/**
	 * Getter for property reason.
	 *
	 * @return Value of property reason.
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * Setter for property description.
	 *
	 * @param description
	 *            New value of property description.
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}

	/**
	 * Getter for property pet.
	 *
	 * @return Value of property pet.
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Setter for property pet.
	 *
	 * @param pet
	 *            New value of property pet.
	 */
	public void setClient(final Client client) {
		this.client = client;
	}

	public AppointmentType getType() {
		return this.type;
	}

	public void setType(final AppointmentType type) {
		this.type = type;
	}

	public Professional getProfessional() {
		return this.professional;
	}

	public void setProfessional(final Professional professional) {
		this.professional = professional;
	}

	public Center getCenter() {
		return this.center;
	}

	public void setCenter(final Center center) {
		this.center = center;
	}

	public LocalDate getDate() {
		return this.Date;
	}

	public void setDate(final LocalDate date) {
		this.Date = date;
	}

	public Diagnosis getDiagnosis() {
		return this.diagnosis;
	}

	public void setDiagnosis(final Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Receipt getReceipt() {
		return this.receipt;
	}

	public void setReceipt(final Receipt receipt) {
		this.receipt = receipt;
	}

	//Relations


	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client			client;

	@ManyToOne
	@JoinColumn(name = "professional_id")
	private Professional	professional;

	@ManyToOne
	@JoinColumn(name = "center_id")
	private Center			center;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "diagnosis_id")
	private Diagnosis		diagnosis;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "receipt_id")
	private Receipt			receipt;

}
