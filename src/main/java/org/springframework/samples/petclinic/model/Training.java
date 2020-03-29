package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

@Data
@Entity
@Table(name = "training")
public class Training extends Booking{
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "trainer_id")
	private Trainer trainer;
	
	@Column(name="groundType")
	@NotNull(message="must not be empty")
	private GroundType groundType;
	
	@Column(name="ground")
	@NotNull(message="must not be empty")
	private Integer ground;
	
    @Override
    public int hashCode() {
        return 31;
    }
}
