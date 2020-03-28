package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Daycare;

public interface DaycareRepository extends CrudRepository<Daycare, Integer> {
	
	List<Daycare> findByPetId(Integer petId);

	Integer countDaycareByDate(@Param("date") LocalDate localDate);

	Integer countDaycareByDateAndPetId(@Param("date") LocalDate localDate, @Param("petId") Integer id);
	
}
