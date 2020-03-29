package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Trainer;

public interface TrainerRepository extends CrudRepository<Trainer, Integer> {

	Collection<Trainer> findByLastName(@Param("lastName") String lastName);

//	Trainer findTrainerByLastName(String trainer);
	
	
}
