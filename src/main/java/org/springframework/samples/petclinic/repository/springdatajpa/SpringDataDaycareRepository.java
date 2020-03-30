package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Daycare;

public interface SpringDataDaycareRepository extends DaycareRepository, CrudRepository<Daycare, Integer>{
	
	@Override
	@Query("SELECT COUNT (*) FROM Daycare daycare WHERE daycare.date=date")
	public Integer countDaycareByDate(@Param("date") LocalDate date);
	
	@Override
	@Query("SELECT COUNT (*) FROM Daycare daycare WHERE daycare.date=date AND pet.id=id")
	public Integer countDaycareByDateAndPetId(@Param("date") LocalDate date, @Param("id") Integer id);

}
