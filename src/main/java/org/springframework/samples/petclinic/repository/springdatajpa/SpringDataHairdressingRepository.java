package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.repository.HairdressingRepository;

public interface SpringDataHairdressingRepository extends HairdressingRepository, Repository<Hairdressing, Integer>{
	@Override
	@Query("select count(*) from Hairdressing h where h.date = :date and h.time = :time")
	Integer countHairdressingsByDateAndTime(@Param("date") LocalDate date, @Param("time") String time);
}
