package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers=TrainerController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class TrainerControllerTests {
	
	private static final int TEST_TRAINER_ID = 1;
	
	@MockBean
	private TrainerService trainerService;
	
	@MockBean
	private TrainingService trainingService;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Trainer trainer;
	
	private Training training;
	
	@BeforeEach
	void setup() {
		trainer = new Trainer();
		trainer.setId(this.TEST_TRAINER_ID);
		trainer.setFirstName("Federico");
		trainer.setLastName("Sartori");
		trainer.setDni("47842798");
		trainer.setEmail("fedsartori45@gmail.com");
		trainer.setSalary(45);
		trainer.setSpecialty("Deportes");
		trainer.setTelephone("095925279");
		trainer.setDescription("Es una persona muy amable");
		
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Iker");
		pet.setType(new PetType());
		pet.setBirthDate(LocalDate.now());
		
		training = new Training();
		this.training.setId(1);
		this.training.setTrainer(this.trainer);
		this.training.setDescription("description");
		this.training.setGround(1);
		this.training.setGroundType(GroundType.AGILIDAD);
		this.training.setDate(LocalDate.now());
		this.training.setPet(pet);
		
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(this.trainer);
	
		this.loadAuthContext();
	}
	
	private void loadAuthContext() {
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitValidCreationFormAsAdmin() throws Exception {
		mockMvc.perform(get("/trainers/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainer"))
			.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {		
		mockMvc.perform(post("/trainers/new")
				.param("firstName", "Federico")
				.param("lastName", "Sartori")
				.with(csrf())
				.param("salary", "45")
				.param("dni", "47842798")
				.param("telephone", "095925279")
				.param("email", "fedsartori45@gmail.com")
				.param("specialty", "Deportes")
				.param("description", "Es una persona muy amable"))
		.andExpect(status().is3xxRedirection());
		
		verify(this.trainerService, times(1)).saveTrainer(any());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationEmptyFormErrors() throws Exception {
		mockMvc.perform(post("/trainers/new").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainer"))
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	
		verify(this.trainerService, times(0)).saveTrainer(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/trainers/new")
				.with(csrf())
				.param("firstName", "Federico"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(model().attributeHasFieldErrors("trainer", "lastName"))
				.andExpect(model().attributeHasFieldErrors("trainer", "dni"))
				.andExpect(model().attributeHasFieldErrors("trainer", "telephone"))
				.andExpect(model().attributeHasFieldErrors("trainer", "email"))
				.andExpect(model().attributeHasFieldErrors("trainer", "specialty"))
				.andExpect(model().attributeHasFieldErrors("trainer", "description"))
				.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	
		verify(this.trainerService, times(0)).saveTrainer(any());
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowTrainer() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", this.TEST_TRAINER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attribute("trainer", hasProperty("firstName", is(this.trainer.getFirstName()))))
		.andExpect(model().attribute("trainer", hasProperty("lastName", is(this.trainer.getLastName()))))
		.andExpect(model().attribute("trainer", hasProperty("dni", is(this.trainer.getDni()))))
		.andExpect(model().attribute("trainer", hasProperty("telephone", is(this.trainer.getTelephone()))))
		.andExpect(model().attribute("trainer", hasProperty("salary", is(this.trainer.getSalary()))))
		.andExpect(model().attribute("trainer", hasProperty("specialty", is(this.trainer.getSpecialty()))))
		.andExpect(model().attribute("trainer", hasProperty("email", is(this.trainer.getEmail()))))
		.andExpect(model().attribute("trainer", hasProperty("description", is(this.trainer.getDescription()))))
		.andExpect(view().name("trainers/trainerDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowInvalidTrainer() throws Exception {
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID))
			.willThrow(NoSuchElementException.class);
		
		mockMvc.perform(get("/trainers/{trainerId}", this.TEST_TRAINER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAllTrainersList() throws Exception {
		given(this.trainerService.findTrainers()).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers/find"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(1)))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(1)).findTrainers();
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAllEmptyTrainersList() throws Exception {
		given(this.trainerService.findTrainers()).willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/trainers/find"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(0)))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(1)).findTrainers();
		verify(this.trainerService, times(0)).findTrainersByLastName("");
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitFindForm() throws Exception {
		given(this.trainerService.findTrainers()).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainers"))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(1)).findTrainers();
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowLastNameTrainersList() throws Exception {
		given(this.trainerService.findTrainersByLastName("Sartori")).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers/find").param("lastName", "Sartori"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(1)))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(0)).findTrainers();
		verify(this.trainerService, times(1)).findTrainersByLastName("Sartori");
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowEmptyLastNameTrainersList() throws Exception {
		given(this.trainerService.findTrainers()).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers/find").param("lastName", ""))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(1)))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(1)).findTrainers();
		verify(this.trainerService, times(0)).findTrainersByLastName("");
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowInvalidLastNameTrainersList() throws Exception {
		given(this.trainerService.findTrainersByLastName("Spektor")).willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/trainers/find").param("lastName", "Spektor"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(0)))
			.andExpect(view().name("trainers/trainersList"));
		
		verify(this.trainerService, times(0)).findTrainers();
		verify(this.trainerService, times(1)).findTrainersByLastName("Spektor");
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowTrainerTrainingsList() throws Exception {
		given(this.trainingService.findTrainingsByTrainer(this.TEST_TRAINER_ID))
			.willReturn(Lists.newArrayList(this.training));
		
		mockMvc.perform(get("/trainers/" + this.TEST_TRAINER_ID + "/trainings"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainings", iterableWithSize(1)))
			.andExpect(view().name("trainings/trainingsList"));
		
		verify(this.trainingService, times(1)).findTrainingsByTrainer(this.TEST_TRAINER_ID);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowInvalidTrainerTrainingsList() throws Exception {
		given(this.trainingService.findTrainingsByTrainer(this.TEST_TRAINER_ID))
			.willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/trainers/" + this.TEST_TRAINER_ID + "/trainings"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainings", iterableWithSize(0)))
			.andExpect(view().name("trainings/trainingsList"));
		
		verify(this.trainingService, times(1)).findTrainingsByTrainer(this.TEST_TRAINER_ID);
	}
}
