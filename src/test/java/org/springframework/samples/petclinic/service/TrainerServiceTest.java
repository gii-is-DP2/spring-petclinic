package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainerServiceTest {

	private static final int TRAINER_ID = 1;
	
	@Autowired
	private TrainerService trainerService;
			
	@Test
	public void shouldFindTrainerById() {
		Trainer foundTrainer = this.trainerService.findTrainerById(this.TRAINER_ID);
		assertThat(foundTrainer).isNotNull();
		assertThat(foundTrainer.getLastName().equals("Balotelli"));
	}
	
	@Test
	void shouldNotFindTrainerById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.trainerService.findTrainerById(45);
		});
	}
	
	@Test
	void shouldFindTrainers() {
		Collection<Trainer> foundTrainer = this.trainerService.findTrainers();
		assertThat(foundTrainer.size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	void shouldFindTrainersByLastName() {				
		Collection<Trainer> foundTrainer = this.trainerService
												.findTrainersByLastName("Balotelli");
		
		assertThat(foundTrainer.size()).isEqualTo(1);
		assertThat(foundTrainer.iterator().next().getLastName().equals("Balotelli"));
	}
	
	@Test
	@Transactional
	void shouldNotFindTrainersByLastName() {
		Collection<Trainer> foundTrainer = this.trainerService.findTrainersByLastName("Aguero");
		
		assertThat(foundTrainer.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void shouldInsertTrainer() {
		Trainer trainer = new Trainer();
		trainer.setFirstName("Federico");
		trainer.setLastName("Sartori");
		trainer.setDescription("Buena persona.");
		trainer.setDni("47842798");
		trainer.setEmail("fedartori@gm.com");
		trainer.setSalary(45);
		trainer.setTelephone("625096668");
		trainer.setSpecialty("Salto");
		
		Integer actualSize = this.trainerService.findTrainers().size();
                
		this.trainerService.saveTrainer(trainer);
		
		Collection<Trainer> foundTrainers = this.trainerService.findTrainers();
		assertThat(foundTrainers.size()).isEqualTo(actualSize + 1 );
		assertTrue(foundTrainers.stream().anyMatch(item -> "Federico".equals(item.getFirstName())));
	}
	
	@Test
	@Transactional
	public void shouldInsertInvalidTrainer() {
		Trainer trainer = new Trainer();
		
		assertThrows(ConstraintViolationException.class, () -> {
				this.trainerService.saveTrainer(trainer);
		});
	}
}
