package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hairdressing;

public interface HairdressingRepository extends CrudRepository<Hairdressing, Integer>{
	Collection<Hairdressing> findByPetId(int id) throws DataAccessException;
}
