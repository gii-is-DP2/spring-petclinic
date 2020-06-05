package org.springframework.samples.petclinic.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.service.CarerService;
import org.springframework.samples.petclinic.service.HairdressingService;
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
	
	@Autowired
	private CarerService carerService;
	
	private Carer testCarer;
	
	@BeforeEach
	private void setup() {
		testCarer = carerService.findCarerById(TEST_CARER_ID);
	}

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
		.andExpect(view().name("carers/createOrUpdateCarerForm"))
		.andExpect(model().attributeExists("carer"));
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
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateFormForbidden() throws Exception {   	
		mockMvc.perform(get("/carer/{carerId}/edit", TEST_CARER_ID))
				.andExpect(status().isForbidden());
	}
	
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitUpdateFormSuccess() throws Exception {
		mockMvc.perform(get("/carers/{carerId}/edit", TEST_CARER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("carer"))
		.andExpect(view().name("carers/createOrUpdateCarerForm"))
		.andExpect(model().attributeExists("carer"))
		.andExpect(model().attribute("carer", hasProperty("isHairdresser", is(testCarer.getIsHairdresser()))))
		.andExpect(model().attribute("carer", hasProperty("firstName", is(testCarer.getFirstName()))))
		.andExpect(model().attribute("carer", hasProperty("lastName", is(testCarer.getLastName()))))
		.andExpect(model().attribute("carer", hasProperty("salary", is(testCarer.getSalary()))))
		.andExpect(model().attribute("carer", hasProperty("dni", is(testCarer.getDni()))))
		.andExpect(model().attribute("carer", hasProperty("email", is(testCarer.getEmail()))))
		.andExpect(model().attribute("carer", hasProperty("telephone", is(testCarer.getTelephone()))));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitUpdateFormInvalidCarer() throws Exception {   	
		mockMvc.perform(get("/carers/{carerId}/edit", 99))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/elementNotFound"));
    }
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormForbidden() throws Exception {
		mockMvc.perform(post("/carers/{carerId}/edit", TEST_CARER_ID)
				.with(csrf())
				.param("firstName", "Ian")
				.param("lastName", " Spektor")
				.param("salary", "2000")
				.param("dni", "11223344i")
				.param("telephone", "666223344")
				.param("email", "correo2@gmail.com")
				.param("isHairdresser", "false"))
			.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {		
		String isHairdresser = "true";
		String firstName = "Alonso";
		String lastName = "Rodriguez";
		String salary = "1000";
		String dni = "11122233444d";
		String telephone = "12335435";
		String email = "correo1@gmail.com";
		
		mockMvc.perform(post("/carers/{carerId}/edit", TEST_CARER_ID)
				.param("firstName", firstName)
				.param("lastName", lastName)
				.param("salary", salary)
				.param("dni", dni)
				.param("telephone", telephone)
				.param("email", email)
				.param("isHairdresser", isHairdresser)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/carers"));
		
		Carer updatedCarer = carerService.findCarerById(TEST_CARER_ID);

		assertThat(String.valueOf(updatedCarer.getIsHairdresser())).isEqualTo(isHairdresser);
		assertThat(updatedCarer.getFirstName()).isEqualTo(firstName);
		assertThat(updatedCarer.getLastName()).isEqualTo(lastName);
		assertThat(updatedCarer.getSalary()).isEqualTo(1000);
		assertThat(updatedCarer.getDni()).isEqualTo(dni);
		assertThat(updatedCarer.getTelephone()).isEqualTo(telephone);
		assertThat(updatedCarer.getEmail()).isEqualTo(email);
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {		
		mockMvc.perform(post("/carers/{carerId}/edit", TEST_CARER_ID)
				.param("firstName", "")
				.param("lastName", "Rodriguez")
				.param("salary", "1000")
				.param("dni", "11223344d")
				.param("telephone", "111223344")
				.param("email", "correo")
				.param("isHairdresser", "true")
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("carer"))
		.andExpect(view().name("carers/createOrUpdateCarerForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationEmptyFormErrors() throws Exception {
		mockMvc.perform(post("/carers/new")
				.param("firstName", "Alonso")
				.param("lastName", "Rodriguez")
				.param("salary", "1000")
				.param("dni", "11223344d")
				.param("telephone", "122334455")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("carer"))
				.andExpect(model().attributeHasErrors("carer"))
				.andExpect(model().attributeHasFieldErrors("carer", "email"))
				.andExpect(model().attributeHasFieldErrors("carer", "isHairdresser"))
				.andExpect(view().name("carers/createOrUpdateCarerForm"));
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
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessDeleteFormSuccessOwner() throws Exception {
		mockMvc.perform(get("/carers/{carerId}/delete", TEST_CARER_ID)
							.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/carers"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessDeleteForbidden() throws Exception {
		mockMvc.perform(get("/carers/{carerId}/delete", TEST_CARER_ID)
							.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessDeleteInvalid() throws Exception {
		mockMvc.perform(get("/carers/{carerId}/delete", 99)
							.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	 
}
