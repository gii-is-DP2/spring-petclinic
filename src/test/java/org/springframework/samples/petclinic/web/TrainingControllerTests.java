package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.util.TrainingDTO;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers=TrainingController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class TrainingControllerTests {
	
	private final int TEST_TRAINING_ID = 1;
	private final int TEST_PET_ID = 1;
	private final int TEST_OWNER_ID = 1;
	private final int TEST_TRAINER_ID = 1;
	
	@MockBean
	private TrainingService trainingService;
	
	@MockBean
	private PetService petService;
	
	@MockBean
    private TrainerService trainerService;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Training training;
	private TrainingDTO trainingDTO;
	private Pet pet;
	private Trainer trainer;
	
	@BeforeEach
	void setup() {
		
		training = new Training();
		training.setId(this.TEST_TRAINING_ID);
		training.setDescription("Descripcion");
		training.setDate(LocalDate.now());
		training.setGround(3);
		training.setGroundType(GroundType.AGILIDAD);
		
		trainer = new Trainer();
		trainer.setDni("12345678A");
		trainer.setDescription("descripsao");
		trainer.setEmail("entrenador@us.es");
		trainer.setFirstName("David");
		trainer.setId(TEST_TRAINER_ID);
		trainer.setLastName("Toro");
		trainer.setSalary(100.0);
		trainer.setSpecialty("Carreras");
		trainer.setTelephone("123456789");
		Set<Training> trainings = new HashSet<Training>();
		trainings.add(training);
		trainer.setTrainings(trainings);

		pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.addTraining(training);
		
		trainingDTO = new TrainingDTO();
		trainingDTO.setDate(training.getDate());
		trainingDTO.setDescription(training.getDescription());
		trainingDTO.setGround(training.getGround());
		trainingDTO.setGroundType(training.getGroundType());
		trainingDTO.setPetName(pet.getName());
		trainingDTO.setTrainerId(this.TEST_TRAINER_ID);
		
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.addPet(pet);
		
		training.setTrainer(trainer);
		training.setPet(pet);
		
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(this.pet);
		given(this.petService.findPetsByOwner(any())).willReturn(Lists.list(this.pet));
		
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/trainings/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(this.trainer);

		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "1")
				.with(csrf()))
		.andExpect(status().is3xxRedirection());
		
		verify(this.trainingService, times(1)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/trainings/new")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "date"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "description"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "ground"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "groundType"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	
		verify(this.trainingService, times(0)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSaveErrors() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(this.trainer);

		
		doThrow(BusinessException.class)
			.when(this.trainingService).saveTraining(any());
		
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	
		verify(this.trainingService, times(1)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormInvalidPet() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString()))
			.willReturn(null);
		
		mockMvc.perform(post("/trainings/new")
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Leo")
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));;
		
		verify(this.trainingService, times(0)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormInvalidTrainer() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID))
			.willThrow(NoSuchElementException.class);

		mockMvc.perform(post("/trainings/new")
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Leo")
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
		
		verify(this.trainingService, times(0)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {   	
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		mockMvc.perform(get("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainingDTO"))
				.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormUnauthorized() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(false);
    	
		mockMvc.perform(get("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	
		verify(this.trainingService, times(0)).saveTraining(any());
    }
    
    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormInvalidTraining() throws Exception {   	
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willThrow(NoSuchElementException.class);
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
   
		mockMvc.perform(get("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/elementNotFound"));
	
		verify(this.trainingService, times(0)).saveTraining(any());
    }
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		mockMvc.perform(post("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID)
							.with(csrf())
							.param("description", "Descrpcion")
							.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
							.param("ground", "3")
							.param("groundType", "AGILIDAD")
							.param("petName","Leo")
							.param("trainerId", "2"))
						.andExpect(status().is3xxRedirection())
						.andExpect(view().name("redirect:/trainings/{trainingId}"));
		
		verify(this.trainingService, times(1)).saveTraining(any());
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormUnauthorized() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(false);
    	
		mockMvc.perform(post("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID)
				.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/accessDenied"));
		
		verify(this.trainingService, times(0)).saveTraining(any());
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
							.with(csrf())
							.param("description", "Descripcion")
							.param("date", "2015/02/12"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasSaveErrors() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		doThrow(BusinessException.class)
		.when(this.trainingService).saveTraining(any());
		
		mockMvc.perform(post("/trainings/{trainingId}/edit", this.TEST_TRAINING_ID)
				.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "2"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormInvalidTraining() throws Exception {
    	given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willThrow(NoSuchElementException.class);
    	
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
    			.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "2"))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/elementNotFound"));
    }
    
    @WithMockUser(value = "spring")
	@Test
    void testProcessUpdateFormInvalidPet() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString()))
			.willReturn(null);
		
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Leo")
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));;
		
		verify(this.trainingService, times(0)).saveTraining(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormInvalidTrainer() throws Exception {
		given(this.petService.findPetsByName(anyString(), anyString())).willReturn(this.pet);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID))
			.willThrow(NoSuchElementException.class);

    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Leo")
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));;
		
		verify(this.trainingService, times(0)).saveTraining(any());
	}
	
    
	@WithMockUser(value = "spring")
	@Test
	void testShowTraining() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
		
		mockMvc.perform(get("/trainings/{trainingId}", this.TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(model().attribute("training", hasProperty("description", is(training.getDescription()))))
			.andExpect(model().attribute("training", hasProperty("date", is(training.getDate()))))
			.andExpect(model().attribute("training", hasProperty("ground", is(training.getGround()))))
			.andExpect(model().attribute("training", hasProperty("groundType", is(training.getGroundType()))))
			.andExpect(view().name("trainings/trainingDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowInvalidTraining() throws Exception {
    	given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willThrow(NoSuchElementException.class);
		
		mockMvc.perform(get("/trainings/{trainingId}", this.TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testShowTrainings() throws Exception {
		given(this.trainingService.findTrainings()).willReturn(Lists.newArrayList(this.training));
		mockMvc.perform(get("/trainings"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainings"))
				.andExpect(view().name("trainings/trainingsList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testShowOwnerTrainings() throws Exception {
		given(this.trainingService.findTrainings()).willReturn(Lists.newArrayList(this.training));
		mockMvc.perform(get("/trainings/owner"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainings"))
				.andExpect(view().name("trainings/trainingsList"));
	}
    
    @WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings"));
	}
    
    @WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(false);
    	
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormOwnerSuccess() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willReturn(this.training);
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/owner"));
	}
	
    @WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testProcessDeleteFormInvalid() throws Exception {
		given(this.trainingService.findTrainingById(TEST_TRAINING_ID)).willThrow(NoSuchElementException.class);
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
    	
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
}
