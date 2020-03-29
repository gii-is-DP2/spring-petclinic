package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class HairdressingServiceTest {

	@Autowired
	private HairdressingService hairdressingService;
	
//	findAll
	@Test
	void shouldfindAll() {
		Iterable<Hairdressing> hairdressing = hairdressingService.findAll();
//		assertThat();
	}
	
//	findHairdressingById
	@Test
	void shouldFindHairDressingWithCorrectId() {
		Hairdressing hairdressing = hairdressingService.findHairdressingById(3).orElse(null);
		assertThat(hairdressing.getCuidado().equals(TipoCuidado.PELUQUERIA));
		assertThat(hairdressing.getDate().equals(LocalDate.now()));
	}
	
	//findByPetId
	@Test
	void shouldFindByPetId() {
		Pet pet = hairdressingService.findByPetId(3);
		//Hay un fallo cierto? XD
	}
	
	//save
	@Test
	void shouldSave() {
		Hairdressing hairdressing;
	}
	
	//delete
	@Test
	void shouldDelete() {
		Hairdressing hairdressing = hairdressingService.findHairdressingById(3).orElse(null);
		int id = hairdressing.getId();
		Pet pet = hairdressingService.findByPetId(id);
		hairdressingService.delete(id);
		assertThat(!pet.getHairdressings().contains(hairdressing));
		assertThat(hairdressingService.findHairdressingById(id).orElse(null) == null);
	}
	
	//countHairdressingsByDateAndTime
	@Test 
	void shouldCountHairdressingByDateAndTime () {
		int count = hairdressingService.countHairdressingsByDateAndTime(LocalDate.now(), "");
		assertThat(count == 3);
	}
}
