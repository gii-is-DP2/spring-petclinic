package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainerServiceTest {

	private TrainerService trainerService;
	private TrainerRepository trainerRepository;
	
	@BeforeEach
	public void initMocks() {
		this.trainerRepository = mock(TrainerRepository.class);
		this.trainerService = new TrainerService(this.trainerRepository);
	}
	
	
	@Test
	public void shouldFindTrainerById() {
		Integer trainerId = 1;
		Trainer trainer = new Trainer();
		trainer.setId(trainerId);
		trainer.setFirstName("Federico");
		when(this.trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
		
		Trainer foundTrainer = this.trainerService.findTrainerById(trainerId);
		verify(this.trainerRepository, times(1)).findById(trainerId);
		assertThat(foundTrainer).isNotNull();
	}
	
	@Test
	void shouldNotFindTrainerById() {
		Integer trainerId = 1;
		when(this.trainerRepository.findById(trainerId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.trainerService.findTrainerById(trainerId);
		});
	}
	
	@Test
	void shouldFindTrainers() {
		Integer trainer1Id = 1;
		Trainer trainer1 = new Trainer();
		trainer1.setId(trainer1Id);
		trainer1.setFirstName("Federico");
		Integer trainer2Id = 2;
		Trainer trainer2 = new Trainer();
		trainer2.setId(trainer2Id);
		trainer2.setFirstName("Federico2");
		Collection<Trainer> trainers = new ArrayList<Trainer>();
		trainers.add(trainer1);
		trainers.add(trainer2);
		
		when(this.trainerRepository.findAll()).thenReturn(trainers);
		
		Collection<Trainer> foundTrainer = this.trainerService.findTrainers();
		verify(this.trainerRepository, times(1)).findAll();
		assertThat(foundTrainer.size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	public void shouldInsertOwner() {
		Trainer trainer = new Trainer();
		trainer.setFirstName("Sam");
		trainer.setLastName("Schultz");
		trainer.setDescription("descripcion");
		trainer.setDni("4545454");
		trainer.setEmail("fedartori@gm.com");
		trainer.setSalary(45);
		trainer.setTelephone("4444444444");               
                
		this.trainerService.saveTrainer(trainer);
		verify(this.trainerRepository, times(1)).save(trainer);
	}
}
