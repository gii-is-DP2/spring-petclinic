package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HairdressingController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class HairdressingControllerTest {

	@Autowired
	private HairdressingController hairdressingController;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private static final int HAIRDRESSING_ID = 99;

	private static final int PET_ID = 1;

	private static final int OWNER_ID = 1;

	@BeforeEach
	void SetUp() {
		Pet pet = petService.findPetById(PET_ID);
		Hairdressing hairdressing = new Hairdressing();

		hairdressing.setId(HAIRDRESSING_ID);
		hairdressing.setCuidado(TipoCuidado.PELUQUERIA);
		hairdressing.setDescription("TEST");
		hairdressing.setPet(pet);
		hairdressing.setDate(LocalDate.of(2022, 02, 02));
		hairdressing.setTime("9.00");
	}

	@WithMockUser(value = "spring")
	@Test
	private void testInitNewHairdressingForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/hairdressing/new", PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("hairdressing"))
				.andExpect(view().name("pets/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	private void testProcessNewHairdressingForm() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/hairdressing/new", OWNER_ID, PET_ID).with(csrf())
				.param("date", "2024-04-04").param("description", "TESTO").param("type", "ESTETICA")
				.param("time", "6:00")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	private void testDeleteHairdressing() throws Exception {

	}
}
