
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "schedule")
public class Schedule extends BaseEntity {

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	Date;

	@Column(name = "start_time")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime	startTime;

	@Column(name = "end_time")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime	endTime;


	public LocalDate getDate() {
		return this.Date;
	}

	public void setDate(final LocalDate date) {
		this.Date = date;
	}

	public LocalTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(final LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(final LocalTime endTime) {
		this.endTime = endTime;
	}


	//Relations

	@ManyToOne
	@JoinColumn(name = "professional_id")
	private Professional	professional;

	@ManyToOne
	@JoinColumn(name = "center_id")
	private Center			center;


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

}
