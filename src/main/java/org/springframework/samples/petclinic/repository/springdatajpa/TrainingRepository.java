package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Training;

public interface TrainingRepository extends CrudRepository<Training, Integer>{
	
	@Query("SELECT DISTINCT tr FROM Training tr WHERE tr.date = :date AND tr.trainer.id = :trainerId")
	public Collection<Training> findByDateAndTrainer(@Param("date") LocalDate date, @Param("trainerId") Integer trainerId);
	
	@Query("SELECT DISTINCT tr FROM Training tr WHERE tr.pet.owner.user.username = :user")
	public Collection<Training> findByUser(@Param("user") String user);
	
	@Query("SELECT DISTINCT tr FROM Training tr WHERE tr.trainer.id = :trainerId")
	public Collection<Training> findByTrainer(@Param("trainerId") Integer trainerId);
}
