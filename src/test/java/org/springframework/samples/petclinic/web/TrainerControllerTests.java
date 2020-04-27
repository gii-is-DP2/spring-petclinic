package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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

import java.util.ArrayList;

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
	
	@Autowired
	private TrainerController trainerController;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Trainer trainer;
	
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
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(this.trainer);
	
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/trainers/new")).andExpect(status().isOk()).andExpect(model().attributeExists("trainer"))
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
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/trainers/new")
				.param("firstName", "Federico")
				.param("lastName", "Sartori")
				.with(csrf())
				.param("salary", "45"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("trainer"))
		.andExpect(model().attributeHasFieldErrors("trainer", "dni"))
		.andExpect(model().attributeHasFieldErrors("trainer", "telephone"))
		.andExpect(model().attributeHasFieldErrors("trainer", "email"))
		.andExpect(model().attributeHasFieldErrors("trainer", "specialty"))
		.andExpect(model().attributeHasFieldErrors("trainer", "description"))
		.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTrainer() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", this.TEST_TRAINER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attribute("trainer", hasProperty("firstName", is("Federico"))))
		.andExpect(model().attribute("trainer", hasProperty("lastName", is("Sartori"))))
		.andExpect(model().attribute("trainer", hasProperty("dni", is("47842798"))))
		.andExpect(model().attribute("trainer", hasProperty("telephone", is("095925279"))))
		.andExpect(model().attribute("trainer", hasProperty("salary", is(45.0))))
		.andExpect(model().attribute("trainer", hasProperty("specialty", is("Deportes"))))
		.andExpect(model().attribute("trainer", hasProperty("email", is("fedsartori45@gmail.com"))))
		.andExpect(model().attribute("trainer", hasProperty("description", is("Es una persona muy amable"))))
		.andExpect(view().name("trainers/trainerDetails"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitFindForm() throws Exception {
		mockMvc.perform(get("/trainers"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainer"))
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAllTrainersList() throws Exception {
		given(this.trainerService.findTrainers()).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers/find").param("lastName", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowLastNameTrainersList() throws Exception {
		given(this.trainerService.findTrainersByLastName("Sartori")).willReturn(Lists.newArrayList(this.trainer));
		
		mockMvc.perform(get("/trainers/find").param("lastName", "Sartori"))
			.andExpect(status().isOk())
			.andExpect(view().name("trainers/trainersList"));
	}

}
