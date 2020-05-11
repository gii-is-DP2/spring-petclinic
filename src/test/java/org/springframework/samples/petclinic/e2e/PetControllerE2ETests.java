package org.springframework.samples.petclinic.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PetControllerE2ETests {

	public final String TEST_OWNER_USERNAME = "fede"; //Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; //Idem
	public final int TEST_PET_ID = 14; //Idem
	public final int TEST_OWNER_ID = 11; //ID correspondiente a fede en data.sql

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
    void testInitCreationFormSuccessOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeExists("pet"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
    void testInitCreationFormSuccessAdmin() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeExists("pet"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
    void testInitCreationFormUnauthorized() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", 1))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testProcessCreationFormSuccessOwner() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
	void testProcessCreationFormSuccessAdmin() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testProcessCreationFormUnauthorized() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 2)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
							.with(csrf())
							.param("name", "Betty"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateFormSuccess() throws Exception {    	
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testInitUpdateFormSuccessAdmin() throws Exception {    	
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateFormUnauthorizedOwner() throws Exception {    	
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", 1, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testInitUpdateFormUnauthorizedPet() throws Exception {    	
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {    	
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessUpdateFormSuccessAdmin() throws Exception {    	
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormUnauthorizedOwner() throws Exception {    	
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormUnauthorizedPet() throws Exception {    	
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, 1)
							.with(csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
							.with(csrf())
							.param("name", "Betty")
							.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}
}
