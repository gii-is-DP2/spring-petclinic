package org.springframework.samples.petclinic.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

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
public class DaycareControllerE2ETests {

	public final String TEST_OWNER_USERNAME = "george"; // Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; // Idem
	public final int TEST_DAYCARE_ID = 1; // Idem

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testNotLoggedIn() throws Exception { // Establecemos en el archivo SecurityConfiguration que para acceder a
												// cualquier ruta precedida
//		 de daycares/ se debe estar autenticado
		mockMvc.perform(get("/daycares/new")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/daycares/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("daycareDTO"))
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testInitCreationFormForbidden() throws Exception {
		mockMvc.perform(get("/daycares/new")).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/daycares/new").with(csrf())).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("daycareDTO"))
				.andExpect(model().attributeHasFieldErrors("daycareDTO", "date"))
				.andExpect(model().attributeHasFieldErrors("daycareDTO", "description"))
				.andExpect(model().attributeHasFieldErrors("daycareDTO", "petName"))
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/daycares/new").
				param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 2) + "/07/06")
				.param("petName", "Leo").
				with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/daycares/*")); // 3 ya que ya existen dos creados por data.sql
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testProcessCreationFormForbidden() throws Exception {
		mockMvc.perform(post("/daycares/new").param("description", "Descripcion")
				.param("date", (LocalDate.now().getYear() + 1) + "/07/06").param("petName", "Leo").with(csrf()))
				.andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testInitUpdateFormForbidden() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/edit", TEST_DAYCARE_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/edit", TEST_DAYCARE_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("daycareDTO"))
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(username = "fede", authorities = { "owner" })
	@Test
	void testInitUpdateFormUnauthorized() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/edit", TEST_DAYCARE_ID)).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testProcessUpdateFormForbidden() throws Exception {
		mockMvc.perform(
				post("/daycares/{daycareId}/edit", TEST_DAYCARE_ID).with(csrf()).param("description", "Descrpcion")
						.param("date", (LocalDate.now().getYear() + 2) + "/05/06").param("petName", "Leo"))
				.andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/daycares/{daycareId}/edit", TEST_DAYCARE_ID).with(csrf())
				.param("description", "Descripcion cambiada").param("date", (LocalDate.now().getYear() + 3) + "/05/12")
				.param("petName", "Leo")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/daycares/owner"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/daycares/{daycareId}/edit", TEST_DAYCARE_ID).with(csrf())
				.param("description", "Descripcion").param("date", "2015/02/12")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("daycareDTO"))
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowDaycareSuccessOwner() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}", TEST_DAYCARE_ID)).andExpect(status().isOk())
				.andExpect(view().name("daycares/daycareDetails"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testShowDaycareSuccessAdmin() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}", TEST_DAYCARE_ID)).andExpect(status().isOk())
				.andExpect(view().name("daycares/daycareDetails"));
	}

	@WithMockUser(username = "fede", authorities = { "owner" })
	@Test
	void testShowDaycareUnauthorized() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}", TEST_DAYCARE_ID)).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testShowDaycaresSuccess() throws Exception {
		mockMvc.perform(get("/daycares")).andExpect(status().isOk()).andExpect(model().attributeExists("daycares"))
				.andExpect(view().name("daycares/daycaresList"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowDaycaresForbidden() throws Exception {
		mockMvc.perform(get("/daycares")).andExpect(status().isOk()).andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowOwnerDaycares() throws Exception {
		mockMvc.perform(get("/daycares/owner")).andExpect(status().isOk())
				.andExpect(model().attributeExists("daycares")).andExpect(view().name("daycares/daycaresList"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testShowOwnerDaycaresForbidden() throws Exception {
		mockMvc.perform(get("/trainings/owner")).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessDeleteFormSuccessOwner() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/delete", TEST_DAYCARE_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/daycares/owner"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testProcessDeleteFormSuccessAdmin() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/delete", TEST_DAYCARE_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/daycares"));
	}

	@WithMockUser(username = "fede", authorities = { "owner" })
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/delete", TEST_DAYCARE_ID).with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessDeleteFormInvalid() throws Exception {
		mockMvc.perform(get("/daycares/{daycareId}/delete", 99)) // Training inexistente
				.andExpect(status().isOk()).andExpect(view().name("errors/elementNotFound"));
	}

}
