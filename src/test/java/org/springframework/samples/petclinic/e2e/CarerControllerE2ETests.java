package org.springframework.samples.petclinic.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class CarerControllerE2ETests {

	public final String TEST_OWNER_USERNAME = "george"; //Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; //Idem
	public final int TEST_CARER_ID = 1; //Idem

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testNotLoggedIn() throws Exception { //La annotation @IsAdmin esta sobre el nombre de la clase
   	//Por ende pruebo en un solo metodo lo que sucede si el usuario no esta loggeado
		mockMvc.perform(get("/carers/**"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/login"));
	}
	 
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testLoggedInAsOwner() throws Exception { //Idem a prueba anterior, pero con user loggeado como owner
		mockMvc.perform(get("/carers"))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/accessDenied"));
	}
	 
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitCreationFormSuccess() throws Exception {
		mockMvc.perform(get("/carers/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("carers/createOrUpdateCarerForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {		
		mockMvc.perform(post("/carers/new")
				.param("firstName", "Alonso")
				.param("lastName", "Rodriguez")
				.param("salary", "1000")
				.param("dni", "11223344d")
				.param("telephone", "111223344")
				.param("email", "correo1@gmail.com")
				.param("isHairdresser", "true")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("/carers/**"));
	}
	
	// hay dudas
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationEmptyFormErrors() throws Exception {
		mockMvc.perform(post("/carers/new")
				.param("firstName", "Alonso")
				.param("lastName", "Rodriguez")
				.param("salary", "1000")
				.param("dni", "11223344d")
				.param("telephone", "1")
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
	void testShowCarer() throws Exception {
		mockMvc.perform(get("/carers/{carerId}", this.TEST_CARER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("carer"))
		.andExpect(view().name("carers/carerDetails"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowInvalidCarer() throws Exception {
		mockMvc.perform(get("/carers/{carerId}", 99))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testShowAllCarersList() throws Exception {
		mockMvc.perform(get("/carers"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("carers"))
			.andExpect(view().name("carers/carersList"));
	}
	 
}
