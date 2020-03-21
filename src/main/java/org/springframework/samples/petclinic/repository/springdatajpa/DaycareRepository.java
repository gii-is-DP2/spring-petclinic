package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Visit;

public interface DaycareRepository extends CrudRepository<Daycare, Integer> {

	void save(Visit visit) throws DataAccessException;
	
	List<Daycare> findByPetId(Integer petId);
}
