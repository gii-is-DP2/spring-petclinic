package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;

public interface HairdressingRepository extends CrudRepository<Hairdressing, Integer>{
	Pet findPetById(int id) throws DataAccessException;
}
