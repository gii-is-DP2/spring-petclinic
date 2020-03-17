package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trainning;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class TrainningService {
	
	private final TrainningRepository trainningRepository;
	
	@Autowired
	public TrainningService(TrainningRepository trainningRepository) {
		this.trainningRepository = trainningRepository;
	}
	
	@Transactional(readOnly = true)
	public Trainning findTrainningById(int id) throws DataAccessException {
		return this.trainningRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public Collection<Trainning> findTrainnings() throws DataAccessException {
		return (Collection<Trainning>) this.trainningRepository.findAll();
	}
	
	@Transactional
	public void saveTrainning(Trainning trainning) throws DataAccessException {
		this.trainningRepository.save(trainning);		
	}
}
