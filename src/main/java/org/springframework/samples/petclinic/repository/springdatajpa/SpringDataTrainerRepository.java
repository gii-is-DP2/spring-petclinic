package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.repository.TrainerRepository;

public interface SpringDataTrainerRepository extends TrainerRepository, CrudRepository<Trainer, Integer>{
	
	@Override
	@Query("SELECT DISTINCT trainer FROM Trainer trainer WHERE trainer.lastName LIKE :lastName%")
	public Collection<Trainer> findByLastName(@Param("lastName") String lastName);
}
