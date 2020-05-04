package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.HairdressingRepository;

public interface SpringDataHairdressingRepository extends HairdressingRepository, Repository<Hairdressing, Integer>{
	@Override
	@Query("select count(*) from Hairdressing h where h.date = :date and h.time = :time")
	Integer countHairdressingsByDateAndTime(@Param("date") LocalDate date, @Param("time") String time);
	
	@Query("SELECT DISTINCT h FROM Hairdressing h WHERE h.pet.owner.user.username = :user")
	public Collection<Hairdressing> findByPetOwnerUserUsername(@Param("user") String user);
}
