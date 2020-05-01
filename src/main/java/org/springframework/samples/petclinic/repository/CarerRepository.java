package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.model.Trainer;

public interface CarerRepository extends CrudRepository<Carer, Integer> {

	Collection<Carer> findByLastName(@Param("lastName") String lastName);
		
}
