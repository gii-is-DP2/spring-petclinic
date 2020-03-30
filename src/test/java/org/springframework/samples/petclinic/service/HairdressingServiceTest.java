package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.repository.HairdressingRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HairdressingServiceTest {

	@Autowired
	private HairdressingService hairdressingService;

	@Autowired
	private PetService petService;
	
	@Autowired
	private HairdressingRepository hairdressingRepository;

//	findAll
	@Test
	
	@BeforeEach
	public void initMocks() {
		hairdressingRepository = mock(HairdressingRepository.class);
		hairdressingService = mock(HairdressingService.class);
	}
	
	
	void shouldfindAll() {
		Iterable<Hairdressing> hairdressing = hairdressingService.findAll();
//		assertThat();
	}

//	findHairdressingById
	@Test
	void shouldFindHairDressingWithId() {
		int id = 1;
//		Hairdressing hairdressing = new Hairdressing();
//		hairdressing.setId(id);
//		hairdressing.setDescription("TEST");
//		
//		when(this.hairdressingRepository.findById(id)).thenReturn(Optional.of(hairdressing));
		
		Hairdressing found = this.hairdressingService.findHairdressingById(id).get();
		verify(this.hairdressingRepository, times(1)).findById(id);
		assertThat(found).isNotNull();
		assertThat(found.getDate().equals(LocalDate.of(2021, 01, 01)));
	}

	// findByPetId
	@Test
	void shouldFindByPetId() {
		Pet pet = hairdressingService.findByPetId(3);
	}

	// save
	@Test
	@Transactional
	void shouldSave() {
		Pet pet = petService.findPetById(1);
		Hairdressing hairdressing = new Hairdressing();

		hairdressing.setId(98);
		hairdressing.setCuidado(TipoCuidado.ESTETICA);
		hairdressing.setDescription("TEST");
		hairdressing.setPet(pet);
		hairdressing.setDate(LocalDate.of(2023, 03, 03));
		hairdressing.setTime("9.00");

		hairdressingService.save(hairdressing);
		Hairdressing hairdressingSaved = hairdressingService.findHairdressingById(hairdressing.getId()).orElse(null);
		assertThat(hairdressingSaved.equals(hairdressing));
	}

	// delete
	@Test
	@Transactional
	void shouldDelete() {
		int id = 99;
		Hairdressing hairdressing = new Hairdressing();
		Pet pet = hairdressing.getPet();
		hairdressing.setCuidado(TipoCuidado.ESTETICA);
		hairdressing.setDate(LocalDate.of(2024, 04, 04));
		hairdressing.setDescription("TESTO");
		hairdressing.setId(id);
		hairdressing.setPet(pet);
		hairdressing.setTime("8:00");
		
		when(hairdressingRepository.findById(id)).thenReturn(Optional.of(hairdressing));
		
		hairdressingService.delete(hairdressing.getId());
		assertThat(!pet.getHairdressings().contains(hairdressing));
		verify(this.hairdressingRepository, times(1)).deleteById(hairdressing.getId());
	}

	// countHairdressingsByDateAndTime
	@Test
	void shouldCountHairdressingByDateAndTime() {
		int count = hairdressingService.countHairdressingsByDateAndTime(LocalDate.of(2021, 01, 01), "9:01");
		assertThat(count == 0);
	}
}
