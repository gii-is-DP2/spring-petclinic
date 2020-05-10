package org.springframework.samples.petclinic.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.NoSuchElementException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TrainerControllerE2ETests {

	public final String TEST_OWNER_USERNAME = "fede"; //Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; //Idem
	public final int TEST_TRAINER_ID = 1; //Idem

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testNotLoggedIn() throws Exception { //La annotation @IsAdmin esta sobre el nombre de la clase
   	//Por ende pruebo en un solo metodo lo que sucede si el usuario no esta loggeado
		mockMvc.perform(get("/trainers/new"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/login"));
	}
	 
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testLoggedInAsOwner() throws Exception { //Idem a prueba anterior, pero con user loggeado como owner
		mockMvc.perform(get("/trainers/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/accessDenied"));
	}
	 
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitCreationFormSuccess() throws Exception {
		mockMvc.perform(get("/trainers/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {		
		mockMvc.perform(post("/trainers/new")
				.param("firstName", "Federico")
				.param("lastName", "Sartori")
				.param("salary", "45")
				.param("dni", "47842798")
				.param("telephone", "095925279")
				.param("email", "fedsartori45@gmail.com")
				.param("specialty", "Deportes")
				.param("description", "Es una persona muy amable")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("/trainers/*"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationEmptyFormErrors() throws Exception {
		mockMvc.perform(post("/trainers/new")
				.param("firstName", "Federico")
				.param("lastName", "Sartori")
				.param("salary", "45")
				.param("dni", "47842798")
				.param("telephone", "095925279")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainer"))
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(model().attributeHasFieldErrors("trainer", "email"))
				.andExpect(model().attributeHasFieldErrors("trainer", "specialty"))
				.andExpect(model().attributeHasFieldErrors("trainer", "description"))
				.andExpect(view().name("trainers/createOrUpdateTrainerForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowTrainer() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", this.TEST_TRAINER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("trainer"))
		.andExpect(view().name("trainers/trainerDetails"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowInvalidTrainer() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", 99))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowAllTrainersList() throws Exception {
		mockMvc.perform(get("/trainers/find"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainers"))
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
    void testInitFindForm() throws Exception {		
		mockMvc.perform(get("/trainers"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainers"))
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowLastNameTrainersList() throws Exception {		
		mockMvc.perform(get("/trainers/find").param("lastName", "Balotelli")) //Pre-creado en data.sql
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(1)))
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowEmptyLastNameTrainersList() throws Exception {
		mockMvc.perform(get("/trainers/find").param("lastName", "Lelelelelele")) //No existe tal trainer
			.andExpect(status().isOk())
			.andExpect(model().attribute("trainers", iterableWithSize(0)))
			.andExpect(view().name("trainers/trainersList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowTrainerTrainingsList() throws Exception {
		mockMvc.perform(get("/trainers/" + this.TEST_TRAINER_ID + "/trainings"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainings"))
			.andExpect(view().name("trainings/trainingsList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowInvalidTrainerTrainingsList() throws Exception {
		mockMvc.perform(get("/trainers/" + 99 + "/trainings"))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	 
}
