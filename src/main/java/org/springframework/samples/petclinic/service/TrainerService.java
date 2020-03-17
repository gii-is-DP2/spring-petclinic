package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class TrainerService {
	
	private final TrainerRepository trainerRepository;
	
	@Autowired
	public TrainerService(TrainerRepository trainerRepository) {
		this.trainerRepository = trainerRepository;
	}
	
	@Transactional(readOnly = true)
	public Trainer findTrainerById(int id) throws DataAccessException {
		return this.trainerRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public Collection<Trainer> findTrainers() throws DataAccessException {
		return (Collection<Trainer>) this.trainerRepository.findAll();
	}
	
	@Transactional
	public void saveTrainer(Trainer trainer) throws DataAccessException {
		this.trainerRepository.save(trainer);		
	}
}
