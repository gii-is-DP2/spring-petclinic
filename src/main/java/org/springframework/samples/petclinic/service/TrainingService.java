package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class TrainingService {
	
	private final TrainingRepository trainingRepository;
	
	@Autowired
	public TrainingService(TrainingRepository trainingRepository) {
		this.trainingRepository = trainingRepository;
	}
	
	@Transactional(readOnly = true)
	public Training findTrainingById(int id) throws DataAccessException {
		return this.trainingRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public Collection<Training> findTrainings() throws DataAccessException {
		return (Collection<Training>) this.trainingRepository.findAll();
	}
	
	@Transactional
	public void saveTraining(Training training) throws DataAccessException {
		this.trainingRepository.save(training);		
	}
}
