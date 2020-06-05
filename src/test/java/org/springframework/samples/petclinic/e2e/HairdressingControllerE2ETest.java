package org.springframework.samples.petclinic.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.samples.petclinic.util.HairdressingDTO;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class HairdressingControllerE2ETest {

	public final String TEST_OWNER_USERNAME = "george";
	public final String TEST_ADMIN_USERNAME = "admin";

	public final int TEST_HAIRDRESSING_ID = 99;
	public final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private HairdressingService hairdressingService;
	
	private Hairdressing testHairdressing;
	
	@BeforeEach
	private void setup() {
		testHairdressing = hairdressingService.findHairdressingById(TEST_HAIRDRESSING_ID);
	}

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

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessNewHairdressingForm() throws Exception {
		mockMvc.perform(post("/hairdressings/new").param("cuidado", "ESTETICA").param("date", "2024/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings/owner"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testProcessNewHairdressingFromAdmin() throws Exception {
		mockMvc.perform(post("/haidressings/new").param("cuidado", "ESTETICA").param("date", "2024/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00").with(csrf()))
				.andExpect(status().isForbidden());
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
		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", this.TEST_HAIRDRESSING_ID)).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"))
				.andExpect(model().attributeExists("hairdressingDTO"))
				.andExpect(model().attribute("hairdressingDTO", hasProperty("cuidado", is(testHairdressing.getCuidado()))))
				.andExpect(model().attribute("hairdressingDTO", hasProperty("description", is(testHairdressing.getDescription()))))
				.andExpect(model().attribute("hairdressingDTO", hasProperty("time", is(testHairdressing.getTime()))))
				.andExpect(model().attribute("hairdressingDTO", hasProperty("date", is(testHairdressing.getDate()))))
				.andExpect(model().attribute("hairdressingDTO", hasProperty("petName", is(testHairdressing.getPet().getName()))));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testUnathorizedUpdateHairdressingForm() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", 90)).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testInitCreateHairdressingFormAdmin() throws Exception {
		mockMvc.perform(get("hairdressings/new", TEST_OWNER_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessUpdateHairdressingForm() throws Exception {
		TipoCuidado cuidado = TipoCuidado.ESTETICA;
		String description = "descri   pcion";
		String petName = "Leo";
		String time = "6:00";
		String date = "2024/04/04";
		
		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.TEST_HAIRDRESSING_ID).with(csrf())
				.param("cuidado", cuidado.toString()).param("date", "2024/04/04").param("description", description)
				.param("petName", petName).param("time", time)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/hairdressings/{hairdressingId}"));
		
		Hairdressing updatedHairdressing = hairdressingService.findHairdressingById(TEST_HAIRDRESSING_ID);
		
		assertThat(updatedHairdressing.getCuidado()).isEqualTo(cuidado);
		assertThat(updatedHairdressing.getDescription()).isEqualTo(description);
		assertThat(updatedHairdressing.getPet().getName()).isEqualTo(petName);
		assertThat(updatedHairdressing.getCuidado()).isEqualTo(TipoCuidado.ESTETICA);
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	public void testProcessUpdateHairdressingFromAdmin() throws Exception {
		mockMvc.perform(post("/haidressings/{haidressingId}/edit", this.TEST_HAIRDRESSING_ID).param("cuidado", "ESTETICA")
				.param("date", "2024/04/04").param("description", "TESTO").param("petName", "Leo").param("time", "6:00")
				.with(csrf())).andExpect(status().isForbidden());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	public void testProcessUpdateHairdressingFormHasErrors() throws Exception {
		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.TEST_HAIRDRESSING_ID).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2015/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(model().attributeHasErrors("hairdressingDTO"))
				.andExpect(model().attributeHasFieldErrors("hairdressingDTO", "date")).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", TEST_HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings/owner"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testProcessDeleteFormSuccessAdmin() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", TEST_HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings"));
	}

	@WithMockUser(username = "betty", authorities = { "owner" })
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", TEST_HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().isOk()).andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testProcessDeleteFormInvalid() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", 200)).andExpect(status().isOk())
				.andExpect(view().name("errors/elementNotFound"));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowHairdressing() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}", this.TEST_HAIRDRESSING_ID)).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/hairdressingDetails"))
				.andExpect(model().attributeExists("hairdressing"))
				.andExpect(model().attribute("hairdressing", hasProperty("cuidado", is(testHairdressing.getCuidado()))))
				.andExpect(model().attribute("hairdressing", hasProperty("description", is(testHairdressing.getDescription()))))
				.andExpect(model().attribute("hairdressing", hasProperty("time", is(testHairdressing.getTime()))))
				.andExpect(model().attribute("hairdressing", hasProperty("date", is(testHairdressing.getDate()))))
				.andExpect(model().attribute("hairdressing", hasProperty("pet", is(testHairdressing.getPet()))));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowHairdressingInvalid() throws Exception {
		mockMvc.perform(get("/hairdressings/{hairdressingId}", 658)).andExpect(view().name("errors/elementNotFound"))
				.andExpect(status().isOk());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowHairdressingList() throws Exception {
		mockMvc.perform(get("/hairdressings/owner")).andExpect(view().name("hairdressings/hairdressingsList"))
				.andExpect(status().isOk()).andExpect(model().attributeExists("hairdressings"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testShowHairdressingListAdmin() throws Exception {
		mockMvc.perform(get("/hairdressings")).andExpect(view().name("hairdressings/hairdressingsList"))
				.andExpect(status().isOk()).andExpect(model().attributeExists("hairdressings"));
	}

	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = { "admin" })
	@Test
	void testShowHairdressingListOwnerFromAdmin() throws Exception {
		mockMvc.perform(get("/hairdressings/owner")).andExpect(view().name("errors/accessDenied"))
				.andExpect(status().isOk());
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = { "owner" })
	@Test
	void testShowHairdressingListAdminFromOwner() throws Exception {
		mockMvc.perform(get("/hairdressings")).andExpect(view().name("errors/accessDenied")).andExpect(status().isOk());
	}
}
