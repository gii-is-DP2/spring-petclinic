package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.repository.HairdressingRepository;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HairdressingServiceTest {

	@Autowired
	private HairdressingService hairdressingService;

	@Autowired	
	private HairdressingRepository hairdressingRepository;

	
	@BeforeEach
	public void initMocks() {
		hairdressingRepository = mock(HairdressingRepository.class);
		hairdressingService = new HairdressingService(hairdressingRepository);
	}

	@Test
	@Transactional
	void shouldDelete() {
		int id = 54;
		Hairdressing hairdressing = new Hairdressing();
		Pet pet = new Pet();
		pet.setId(id);
		hairdressing.setCuidado(TipoCuidado.ESTETICA);
		hairdressing.setDate(LocalDate.of(2024, 04, 04));
		hairdressing.setDescription("TESTO");
		hairdressing.setId(id);
		hairdressing.setPet(pet);
		hairdressing.setTime("8:00");
		
		when(hairdressingRepository.findById(id)).thenReturn(Optional.of(hairdressing));
		
		hairdressingService.delete(id);
		verify(this.hairdressingRepository, times(1)).deleteById(id);
	}
}
