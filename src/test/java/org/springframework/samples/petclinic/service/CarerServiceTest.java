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
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CarerServiceTest {

	
	private static final int CARER_ID = 1;
	
	@Autowired
	private CarerService carerService;
			
	@Test
	public void shouldFindCarerrById() {
		Carer foundCarer = this.carerService.findCarerById(this.CARER_ID);
		assertThat(foundCarer).isNotNull();
		assertThat(foundCarer.getLastName().equals("Rodriguez"));
	}
	
	@Test
	void shouldNotFindCarerById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.carerService.findCarerById(45);
		});
	}
	
	@Test
	void shouldFindCarers() {
		Collection<Carer> foundCarer = this.carerService.findCarers();
		assertThat(foundCarer.size()).isEqualTo(3);
	}
	
	@Test
	@Transactional
	void shouldFindCarersByLastName() {				
		Collection<Carer> foundCarer = this.carerService
												.findCarersByLastName("Rodriguez");
		
		assertThat(foundCarer.size()).isEqualTo(1);
		assertThat(foundCarer.iterator().next().getLastName().equals("Rodriguez"));
	}
	
	@Test
	@Transactional
	void shouldNotFindCarersByLastName() {
		Collection<Carer> foundCarer = this.carerService.findCarersByLastName("Portgas");
		
		assertThat(foundCarer.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void shouldInsertCarer() {
		Carer carer = new Carer();
		carer.setFirstName("Eustass");
		carer.setLastName("Kid");
		carer.setDni("424442428");
		carer.setEmail("captainkid@gmail.com");
		carer.setSalary(50);
		carer.setTelephone("666555777");
		carer.setIsHairdresser(false);
		
		Integer actualSize = this.carerService.findCarers().size();
                
		this.carerService.saveCarer(carer);
		
		Collection<Carer> foundCarers = this.carerService.findCarers();
		assertThat(foundCarers.size()).isEqualTo(actualSize + 1 );
		assertTrue(foundCarers.stream().anyMatch(item -> "Eustass".equals(item.getFirstName())));
	}
	
	@Test
	@Transactional
	public void shouldNotInsertInvalidCarer() {
		Carer carer = new Carer();
		
		assertThrows(ConstraintViolationException.class, () -> {
				this.carerService.saveCarer(carer);
		});
	}
	
	
}
