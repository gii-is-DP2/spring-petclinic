package org.springframework.samples.petclinic.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TrainingControllerE2ETests {

	public final String TEST_OWNER_USERNAME = "fede"; //Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; //Idem
	public final int TEST_TRAINING_ID = 1; //Idem

	@Autowired
	private MockMvc mockMvc;
	
	 @Test
	void testNotLoggedIn() throws Exception { //La annotation @IsAuthenticated esta sobre el nombre de la clase
    	//Por ende pruebo en un solo metodo lo que sucede si el usuario no esta loggeado
		mockMvc.perform(get("/trainings/new"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/login"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/trainings/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
    void testInitCreationFormForbidden() throws Exception {
		mockMvc.perform(get("/trainings/new"))
			.andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
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
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormSaveInvalidDate() throws Exception {
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() - 1) + "/05/06") //Fecha invalida
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker") 
				.param("trainerId", "1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormSaveOccupiedTrainer() throws Exception {
		String date = (LocalDate.now().getYear() + 1) + "/05/06";
		String trainerId = "1";
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", date)
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker") 
				.param("trainerId", trainerId)
				.with(csrf()));
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", date) //Fecha invalida
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker") 
				.param("trainerId", trainerId)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormInvalidPet() throws Exception {
		mockMvc.perform(post("/trainings/new")
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Pedro") //No existe esta mascota
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));;
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormInvalidTrainer() throws Exception {
		mockMvc.perform(post("/trainings/new")
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Iker")
			.param("trainerId", "99") //No existe este trainer
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/07/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker") //Se crea al levantar la BD con data.sql
				.param("trainerId", "1")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trainings/2")); //2 ya que ya existe uno creado por data.sql
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationFormForbidden() throws Exception {
		mockMvc.perform(post("/trainings/new")
				.param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/07/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker") 
				.param("trainerId", "1")
				.with(csrf()))
		.andExpect(status().isForbidden()); 
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitUpdateFormForbidden() throws Exception {   	
		mockMvc.perform(get("/trainings/{trainingId}/edit", TEST_TRAINING_ID))
				.andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/edit", TEST_TRAINING_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = "george", authorities = {"owner"})
	@Test
	void testInitUpdateFormUnauthorized() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/edit", TEST_TRAINING_ID)) //Creado en data.sql con mascota de "fede"
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
    }
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateFormInvalidTraining() throws Exception {   	
		mockMvc.perform(get("/trainings/{trainingId}/edit", 99)) //Training inexistente
		.andExpect(status().isOk())
		.andExpect(view().name("errors/elementNotFound"));
    }
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessUpdateFormForbidden() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
				.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "2"))
			.andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = "george", authorities = {"owner"})
	@Test
	void testProcessUpdateFormUnauthorized() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID) //Idem a testInitUpdateFormUnauthorized
				.with(csrf())
				.param("description", "Nueva descri")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker")
				.param("trainerId", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
				.with(csrf())
				.param("description", "Descripcion cambiada")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/12")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker")
				.param("trainerId", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/trainings/{trainingId}"));		
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
							.with(csrf())
							.param("description", "Descripcion")
							.param("date", "2015/02/12"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "ground"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "groundType"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormHasSaveErrors() throws Exception {
		mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
				.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() - 1) + "/05/06") //Fecha invalida
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Leo")
				.param("trainerId", "2"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormInvalidTraining() throws Exception {    	
    	mockMvc.perform(post("/trainings/{trainingId}/edit", 99) //Training inexistente
    			.with(csrf())
				.param("description", "Descrpcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
				.param("ground", "3")
				.param("groundType", "AGILIDAD")
				.param("petName","Iker")
				.param("trainerId", "2"))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/elementNotFound"));
    }
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
    void testProcessUpdateFormInvalidPet() throws Exception {
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Pedrito") //Mascota inexistente
			.param("trainerId", "1")
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "petName"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));;
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormInvalidTrainer() throws Exception {
    	mockMvc.perform(post("/trainings/{trainingId}/edit", TEST_TRAINING_ID)
			.param("description", "Descripcion")
			.param("date", (LocalDate.now().getYear() + 1) + "/05/06")
			.param("ground", "3")
			.param("groundType", "AGILIDAD")
			.param("petName","Iker")
			.param("trainerId", "99") //Trainer inexistente
			.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trainingDTO"))
			.andExpect(model().attributeHasFieldErrors("trainingDTO", "trainerId"))
			.andExpect(view().name("trainings/createOrUpdateTrainingForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testShowTrainingSuccessOwner() throws Exception {		
		mockMvc.perform(get("/trainings/{trainingId}", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/trainingDetails"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowTrainingSuccessAdmin() throws Exception {		
		mockMvc.perform(get("/trainings/{trainingId}", TEST_TRAINING_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("trainings/trainingDetails"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testShowInvalidTraining() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}", 99)) //Training inexistente
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}

	@WithMockUser(username = "george", authorities = {"owner"})
	@Test
	void testShowTrainingUnauthorized() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}", TEST_TRAINING_ID)) 
			.andExpect(status().isOk())
			.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
	void testShowTrainingsSuccess() throws Exception {
		mockMvc.perform(get("/trainings"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainings"))
				.andExpect(view().name("trainings/trainingsList"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testShowTrainingsForbidden() throws Exception {
		mockMvc.perform(get("/trainings"))
				.andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testShowOwnerTrainings() throws Exception {
		mockMvc.perform(get("/trainings/owner"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainings"))
				.andExpect(view().name("trainings/trainingsList"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
	void testShowOwnerTrainingsForbidden() throws Exception {
		mockMvc.perform(get("/trainings/owner"))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessDeleteFormSuccessOwner() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings/owner"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessDeleteFormSuccessAdmin() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/trainings"));
	}
	
	@WithMockUser(username = "george", authorities = {"admin"})
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/delete", TEST_TRAINING_ID)
							.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessDeleteFormInvalid() throws Exception {
		mockMvc.perform(get("/trainings/{trainingId}/delete", 99)) //Training inexistente
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
}
