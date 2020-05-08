package org.springframework.samples.petclinic.e2e;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
public class HairdressingControllerE2Test {

	public final String TEST_OWNER_USERNAME = "george";
	public final String TEST_ADMIN_USERNAME = "admin";

	public final int TEST_HAIRDRESSING = 99;
	public final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testInitNewHairdressingForm() throws Exception {
		mockMvc.perform(get("/hairdressings/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("hairdressingDTO"))
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testInitNewHairdressingFormAdmin() throws Exception {
		mockMvc.perform(get("hairdressings/new", TEST_OWNER_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testUnathorizedNewHairdressingForm() throws Exception {
		//	TODO
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessNewHairdressingForm() throws Exception {
		mockMvc.perform(post("/hairdressings/new").param("cuidado", "ESTETICA").param("date", "2024/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessNewHairdressingFormHasErrors() throws Exception {
		mockMvc.perform(post("/hairdressings/new").param("time", "6:00").with(csrf()))
				.andExpect(model().attributeHasErrors("hairdressingDTO"))
				.andExpect(model().attributeHasFieldErrors("hairdressingDTO", "date")).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));

	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testInitUpdateHairdressingForm() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", this.TEST_HAIRDRESSING)).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testUnathorizedUpdateHairdressingForm() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", 90)).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testInitUpdateHairdressingFormAdmin() throws Exception {
		mockMvc.perform(get("hairdressings/new", TEST_OWNER_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessUpdateHairdressingForm() throws Exception {
		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.TEST_HAIRDRESSING).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2024/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/hairdressings/{hairdressingId}"));
	}
	
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessUpdateHairdressingFormHasErrors() throws Exception {
		//	TODO
	}
}
