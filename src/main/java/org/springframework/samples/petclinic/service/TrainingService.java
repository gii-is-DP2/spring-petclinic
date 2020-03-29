package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainingRepository;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Function;

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
	public void saveTraining(Training training) throws DataAccessException, BusinessException {
		if (this.existsByDateAndTrainer(training.getDate(), training.getTrainer().getId())) {
			throw new BusinessException("trainerId", "unique", "The trainer already has another training on that date.");
		}
		this.trainingRepository.save(training);
	}
	
	@Transactional
	public void delete(int trainingId) throws DataAccessException {
		Training training = this.findTrainingById(trainingId);
		Pet pet = training.getPet();
		pet.removeTraining(training.getId());
		this.trainingRepository.deleteById(trainingId);
	}
	
	@Transactional
	public void deleteTraining(Training training) throws DataAccessException {
		this.trainingRepository.delete(training);
	}
	
	@Transactional(readOnly = true)
	public boolean existsByDateAndTrainer(LocalDate date, int trainerId) {
		Collection<Training> trainings = this.trainingRepository.findByDateAndTrainer(date, trainerId);
		return !trainings.isEmpty();
	}
}
