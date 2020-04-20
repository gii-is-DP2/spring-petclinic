package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Hairdressing;

public interface HairdressingRepository extends CrudRepository<Hairdressing, Integer>{
	Collection<Hairdressing> findByPetId(int id) throws DataAccessException;

	Collection<Hairdressing> findByPetOwnerUserUsername(@Param("user") String user) throws DataAccessException;

	Integer countHairdressingsByDateAndTime(@Param("date") LocalDate date, @Param("time") String time) throws DataAccessException;

}
