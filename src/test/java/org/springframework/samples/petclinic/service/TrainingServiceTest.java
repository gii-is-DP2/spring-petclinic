package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TipoPista;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainingServiceTest {
	
	private TrainingService trainingService;
	private TrainingRepository trainingRepository;
	
	@BeforeEach
	public void initMocks() {
		this.trainingRepository = mock(TrainingRepository.class);
		this.trainingService = new TrainingService(this.trainingRepository);
	}
	
	@Test
	public void shouldFindTrainingById() {
		Integer trainingId = 1;
		Training training = new Training();
		training.setId(trainingId);
		training.setDescription("Descripcion de entrenamiento");
		when(this.trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
		
		Training foundTraining = this.trainingService.findTrainingById(trainingId);
		verify(this.trainingRepository, times(1)).findById(trainingId);
		assertThat(foundTraining).isNotNull();
		assertThat(foundTraining.getDescription()).isEqualTo(training.getDescription());
	}
	
	@Test
	void shouldNotFindTrainingById() {
		Integer trainingId = 1;
		when(this.trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.trainingService.findTrainingById(trainingId);
		});
	}
	
	@Test
	void shouldFindTrainings() {
		Integer training1Id = 1;
		Training training1 = new Training();
		training1.setId(training1Id);
		training1.setDescription("Descripcion 1");
		Integer training2Id = 2;
		Training training2 = new Training();
		training2.setId(training2Id);
		training2.setDescription("Descripcion 2");
		Collection<Training> trainings = new ArrayList<Training>();
		trainings.add(training1);
		trainings.add(training2);
		
		when(this.trainingRepository.findAll()).thenReturn(trainings);
		
		Collection<Training> foundTrainings = this.trainingService.findTrainings();
		verify(this.trainingRepository, times(1)).findAll();
		assertThat(foundTrainings.size()).isEqualTo(2);
		assertThat(foundTrainings).contains(training1);
		assertThat(foundTrainings).contains(training2);
	}
	
	@Test
	@Transactional
	public void shouldInsertTraining() {
		Training training = new Training();
		training.setDescription("Sam");
		training.setDate(LocalDate.now());
		training.setPista(3);
		training.setTipoPista(TipoPista.OBEDIENCIA);             
                
		this.trainingService.saveTraining(training);
		verify(this.trainingRepository, times(1)).save(training);
	}
	
	@Test
	@Transactional
	public void shouldDeleteTraining() {
		Training training = new Training();
		training.setDescription("Sam");
		training.setDate(LocalDate.now());
		training.setPista(3);
		training.setTipoPista(TipoPista.OBEDIENCIA);             
                
		this.trainingService.deleteTraining(training);
		verify(this.trainingRepository, times(1)).delete(training);
	}
	
	
}
