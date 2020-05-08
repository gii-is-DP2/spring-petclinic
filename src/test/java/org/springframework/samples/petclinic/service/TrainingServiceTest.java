package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainingServiceTest {
	
	private final int TRAINER_ID = 2;
	private final int PET_ID = 1;
	
	private Training training;
	private User user;
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private TrainerService trainerService;
	
	private int addTraining(LocalDate date) throws DataAccessException, BusinessException {
		this.training = new Training();
		this.training.setDescription("Descripcion");
		this.training.setDate(date);
		this.training.setGround(1);
		this.training.setGroundType(GroundType.OBEDIENCIA);
		this.training.setPet(this.petService.findPetById(this.PET_ID));
		this.training.setTrainer(this.trainerService.findTrainerById(this.TRAINER_ID));
		
		this.trainingService.saveTraining(training);
		
		this.user = this.training.getPet().getOwner().getUser();
		
		return training.getId();
	}
	
	@Test
	@Transactional
	public void shouldFindTrainingById() throws DataAccessException, BusinessException {
		int id = this.addTraining(LocalDate.now().plusDays(5));

		Training foundTraining = this.trainingService.findTrainingById(id);
		
		assertThat(foundTraining).isNotNull();
		assertThat(foundTraining.getDate()).isEqualTo(training.getDate());
		assertThat(foundTraining.getDescription()).isEqualTo(training.getDescription());
		assertThat(foundTraining.getGround()).isEqualTo(training.getGround());
		assertThat(foundTraining.getGroundType()).isEqualTo(training.getGroundType());
		assertThat(foundTraining.getId()).isEqualTo(id);
		assertThat(foundTraining.getPet().getId()).isEqualTo(training.getPet().getId());
		assertThat(foundTraining.getTrainer().getId()).isEqualTo(training.getTrainer().getId());
	}
	
	@Test
	@Transactional
	void shouldNotFindTrainingById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.trainingService.findTrainingById(99);
		});
	}
	
	@Test
	@Transactional
	void shouldFindTrainings() throws DataAccessException, BusinessException {
		int previousSize = this.trainingService.findTrainings().size();
		this.addTraining(LocalDate.now().plusDays(5));
		Collection<Training> foundTrainings = this.trainingService.findTrainings();
		assertThat(foundTrainings.size()).isEqualTo(previousSize + 1);
	}
	
	@Test
	@Transactional
	void shouldFindTrainingsByUser() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		Collection<Training> foundTrainings = this.trainingService.findTrainingsByUser(this.user.getUsername());
		assertThat(foundTrainings.size()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindTrainingsByUser() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		Collection<Training> foundTrainings = this.trainingService.findTrainingsByUser("betty");
		assertThat(foundTrainings.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldFindTrainingsByTrainer() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		Collection<Training> foundTrainings = this.trainingService.findTrainingsByTrainer(this.training.getTrainer().getId());
		assertThat(foundTrainings.size()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindTrainingsByTrainer() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		Collection<Training> foundTrainings = this.trainingService.findTrainingsByTrainer(45);
		assertThat(foundTrainings.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void shouldInsertTraining() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		int previousTrainings = this.trainingService.findTrainings().size();
		Training newTraining = new Training();
		newTraining.setDescription("Sam");
		newTraining.setDate(LocalDate.now().plusDays(2));
		newTraining.setGround(3);
		newTraining.setGroundType(GroundType.OBEDIENCIA); 
		newTraining.setTrainer(this.trainerService.findTrainerById(TRAINER_ID));
		newTraining.setPet(this.petService.findPetById(PET_ID));
                
		this.trainingService.saveTraining(newTraining);
		
		Collection<Training> actualTrainings = this.trainingService.findTrainings();
		assertThat(actualTrainings.size()).isEqualTo(previousTrainings + 1);
	}
	
	@Test
	@Transactional
	public void shouldNotInsertSameTrainerDateTraining() throws DataAccessException, BusinessException {
		this.addTraining(LocalDate.now().plusDays(5));
		Assertions.assertThrows(BusinessException.class, () -> {
			this.addTraining(LocalDate.now().plusDays(5));
		});
	}
	
	@Test
	@Transactional
	public void shouldNotInsertPastDateTraining() throws DataAccessException, BusinessException {
		Assertions.assertThrows(BusinessException.class, () -> {
			this.addTraining(LocalDate.now().minusDays(5));
		});
	}
	
	@Test
	public void shouldDeleteByTrainingId() throws DataAccessException, BusinessException {
		int id = this.addTraining(LocalDate.now().plusDays(5));
		int previousTrainings = this.trainingService.findTrainings().size();
		
		this.trainingService.delete(id);
		
		Collection<Training> actualTrainings = this.trainingService.findTrainings();
		assertThat(actualTrainings.size()).isEqualTo(previousTrainings - 1);
		assertThat(actualTrainings.stream().map(t -> t.getId())).doesNotContain(id);
	}
	
	@Test
	public void shouldNotDeleteInvalidTrainingById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.trainingService.delete(99);
		});
	}
	
	@Test
	public void shouldDeleteByTraining() throws DataAccessException, BusinessException {
		int id = this.addTraining(LocalDate.now().plusDays(5));
		int previousTrainings = this.trainingService.findTrainings().size();
		
		this.trainingService.deleteTraining(this.training);
		
		Collection<Training> actualTrainings = this.trainingService.findTrainings();
		assertThat(actualTrainings.size()).isEqualTo(previousTrainings - 1);
		assertThat(actualTrainings.stream().map(t -> t.getId())).doesNotContain(id);
	}
}
