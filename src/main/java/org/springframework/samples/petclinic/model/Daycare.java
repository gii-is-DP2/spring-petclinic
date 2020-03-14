package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "daycare")
public class Daycare extends Booking{
	private Integer aforo;
}
