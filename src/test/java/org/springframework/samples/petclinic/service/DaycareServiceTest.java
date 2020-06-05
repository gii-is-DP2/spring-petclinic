package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.springdatajpa.DaycareRepository;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class DaycareServiceTest {
	
	@Autowired
	private DaycareService daycareService;
	
	@Autowired
	private DaycareRepository daycareRepository;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private OwnerService ownerService;
	
	@BeforeEach
	public void initMocks() {
		this.daycareRepository = mock(DaycareRepository.class);
		this.daycareService = new DaycareService(this.daycareRepository);
	}
	
	@Test
	public void shouldFindDaycareById() {
		Integer daycareId = 1 ;
		Daycare daycare = new Daycare();
		daycare.setId(daycareId);
		daycare.setDescription("descripcion ejemplo");
		
		when(this.daycareRepository.findById(daycareId)).thenReturn(Optional.of(daycare));
		
		Daycare foundDaycare = this.daycareService.findDaycareById(daycareId);
		verify(this.daycareRepository, times(1)).findById(daycareId);	
		assertThat(foundDaycare).isNotNull();
		assertThat(foundDaycare.getDescription()).isEqualTo(daycare.getDescription());
		
	}
	
	@Test
	void shouldNotFindDaycareById() {
		Integer daycareId = 1;
		when(this.daycareRepository.findById(daycareId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.daycareService.findDaycareById(daycareId);
		});
	}
	
	@Test
	void shouldFindDaycares() {
		Integer daycare1Id = 1;
		Daycare daycare1 = new Daycare();
		Pet pet1 = new Pet();
		pet1.setId(1);
		daycare1.setId(daycare1Id);
		daycare1.setDescription("Descripcion 1");
		daycare1.setPet(pet1);
		pet1.addDaycare(daycare1);
		Integer daycare2Id = 2;
		Daycare daycare2 = new Daycare();
		daycare2.setId(daycare2Id);
		daycare2.setDescription("Descripcion 2");
		daycare2.setPet(pet1);
		pet1.addDaycare(daycare1);
		pet1.addDaycare(daycare2);
		List<Daycare> daycares = new ArrayList<Daycare>();
		daycares.add(daycare1);
		daycares.add(daycare2);
		
		when(this.daycareRepository.findByPetId(pet1.getId())).thenReturn(daycares);
		
		Collection<Daycare> foundDaycares = this.daycareService.findDaycaresByPetId(1);
		verify(this.daycareRepository, times(1)).findByPetId(pet1.getId());
		assertThat(foundDaycares.size()).isEqualTo(2);
		assertThat(foundDaycares).contains(daycare1);
		assertThat(foundDaycares).contains(daycare2);
	}
	
	@Test
	@Transactional
	public void shouldInsertDaycare() {
		Daycare daycare = new Daycare();
		Pet pet = new Pet();
		pet.setId(1);
		daycare.setDescription("asdasd");
		daycare.setDate(LocalDate.now());
		daycare.setCapacity(15);
		daycare.setPet(pet);           
                
		this.daycareService.saveDaycare(daycare);
		verify(this.daycareRepository, times(1)).save(daycare);
	}
	
	
	@Test
	@Transactional
	public void shouldDeleteDaycareById() {
	
		Daycare daycare = new Daycare();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setBirthDate(LocalDate.now());
		pet.setName("alonso");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		daycare.setId(1);
		daycare.setDescription("asdasd");
		daycare.setDate(LocalDate.now());
		daycare.setCapacity(15);
		daycare.setPet(pet);   
		pet.addDaycare(daycare);
		Owner owner6 = this.ownerService.findOwnerById(6);
		owner6.addPet(pet);
		
		when(this.daycareRepository.findById(daycare.getId())).thenReturn(Optional.of(daycare));
		
		this.daycareService.delete(daycare.getId());
		verify(this.daycareRepository, times(1)).deleteById(daycare.getId());
	}
	
	@Test
	public void shouldNotDeleteInvalidDaycareById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.daycareService.delete(1234);
		});
	}
	
	@Test
	@Transactional
	public void shouldCountOneDaycareByDate() {
		Daycare daycare = new Daycare();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setBirthDate(LocalDate.now());
		pet.setName("alonso");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		daycare.setId(1);
		daycare.setDescription("asdasd");
		daycare.setDate(LocalDate.now());
		daycare.setCapacity(15);
		daycare.setPet(pet);   
		pet.addDaycare(daycare);
		Owner owner6 = this.ownerService.findOwnerById(6);
		owner6.addPet(pet);
		
		when(this.daycareRepository.countDaycareByDateAndPetId(daycare.getDate(), pet.getId())).thenReturn(1);
		
		this.daycareService.oneDaycareById(daycare.getDate(), 1);
		verify(this.daycareRepository, times(1)).countDaycareByDateAndPetId(daycare.getDate(), 1);
	}
	
	@Test
	@Transactional
	public void shouldNotCountOneDaycareByDate() {
		
		
		when(this.daycareRepository.countDaycareByDateAndPetId(LocalDate.now().plusDays(2) , 2 )).thenReturn(0);
		
		assertThat(this.daycareService.oneDaycareById(LocalDate.now().plusDays(2) , 2) == 0);
		verify(this.daycareRepository, times(1)).countDaycareByDateAndPetId(LocalDate.now().plusDays(2) , 2);
	}
	
	
}
