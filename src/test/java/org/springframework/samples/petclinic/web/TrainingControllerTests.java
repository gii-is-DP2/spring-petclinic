package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers=TrainingController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class TrainingControllerTests {
	
	private static final int TEST_TRAINING_ID = 1;
	
	@MockBean
	private TrainingService trainingService;
	
	@Autowired
	private TrainingController trainingController;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Training training;
	
	@BeforeEach
	void setup() {
		training = new Training();
		training.setId(this.TEST_TRAINING_ID);
		training.setDescription("Descripcion");
		training.setDate(LocalDate.now());
		training.setGround(3);
		training.setGroundType(GroundType.AGILIDAD);
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/trainings/new")).andExpect(status().isOk()).andExpect(model().attributeExists("training"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", "2020/05/06")
				.param("pista", "3")
				.param("tipoPista", "AGILIDAD")
				.with(csrf()))
		.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", "2020/05/06")
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("training"))
		.andExpect(model().attributeHasFieldErrors("training", "pista"))
		.andExpect(model().attributeHasFieldErrors("training", "tipoPista"))
		.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTraining() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}", this.TEST_TRAINING_ID))
		.andExpect(status().isOk())
		.andExpect(model().attribute("training", hasProperty("description", is(training.getDescription()))))
		.andExpect(model().attribute("training", hasProperty("date", is(training.getDate()))))
		.andExpect(model().attribute("training", hasProperty("pista", is(training.getGround()))))
		.andExpect(model().attribute("training", hasProperty("tipoPista", is(training.getGroundType()))))
		.andExpect(view().name("trainings/trainingDetails"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testShowTrainings() throws Exception {
		given(this.trainingService.findTrainings()).willReturn(Lists.newArrayList(this.training));
		mockMvc.perform(get("/trainings"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainings")).andExpect(view().name("trainings/trainingsList"));
	}

    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/trainings/{petId}/edit", TEST_TRAINING_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("training"))
				.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
							.with(csrf())
							.param("description", "Descripcion")
							.param("date", "2020/05/03")
							.param("pista", "2")
							.param("tipoPista", "AGILIDAD"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/{trainingId}"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
							.with(csrf())
							.param("description", "Descripcion")
							.param("date", "2015/02/12"))
				.andExpect(model().attributeHasErrors("training")).andExpect(status().isOk())
				.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		mockMvc.perform(post("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings"));
	}
	
	
}
