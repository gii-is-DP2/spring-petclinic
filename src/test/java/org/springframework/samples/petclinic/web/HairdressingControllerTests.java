package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.collect.Lists;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@WebMvcTest(controllers = HairdressingController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class HairdressingControllerTests {

	private static final int HAIRDRESSING_ID = 99;

	private static final int PET_ID = 1;

	private static final int OWNER_ID = 1;

	@MockBean
	private PetService petService;

	@MockBean
	private HairdressingService hairdressingService;

	@MockBean
	private AuthorizationService authorizationService;

	@MockBean
	private Authentication auth;

	@MockBean
	private SecurityContext securityContext;

	@Autowired
	private HairdressingController hairdressingController;

	@Autowired
	private MockMvc mockMvc;

	private Pet pet;

	private Hairdressing hairdressing;

	@BeforeEach
	void setUp() {
		hairdressing = new Hairdressing();
		pet = new Pet();

		hairdressing.setId(HAIRDRESSING_ID);
		hairdressing.setCuidado(TipoCuidado.PELUQUERIA);
		hairdressing.setDescription("TEST");
		hairdressing.setPet(pet);
		hairdressing.setDate(LocalDate.of(2022, 02, 02));
		hairdressing.setTime("9.00");
		
		Owner owner = new Owner();
		owner.setId(1);
		owner.addPet(pet);
		
		pet.setBirthDate(LocalDate.now());
		pet.setId(PET_ID);
		pet.setName("elpotro");

		given(this.petService.findPetById(PET_ID)).willReturn(this.pet);
		given(this.hairdressingService.findHairdressingById(HAIRDRESSING_ID)).willReturn(this.hairdressing);

		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}

	@WithMockUser(value = "spring")
	@Test
	public void testInitNewHairdressingForm() throws Exception {
		mockMvc.perform(get("/hairdressings/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("hairdressingDTO"))
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	public void testInitNewHairdressingFormAdmin() throws Exception {
		mockMvc.perform(get("/hairdressings/new")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testProcessNewHairdressingForm() throws Exception {
		mockMvc.perform(post("/hairdressings/new").with(csrf()).param("cuidado", "ESTETICA").param("date", "2022/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings/owner"));
	}
	
	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	public void testProcessNewHairdressingFormAdmin() throws Exception {
		mockMvc.perform(post("/hairdressings/new").with(csrf()).param("cuidado", "ESTETICA").param("date", "2022/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testProcessNewHairdressingFormErrors() throws Exception {
		mockMvc.perform(post("/hairdressings/new").with(csrf()).param("cuidado", "ESTETICA").param("date", "2015/04/04")
				.param("description", "TESTO").param("petName", "Leo").param("time", "6:00"))
				.andExpect(model().attributeHasErrors("hairdressingDTO"))
				.andExpect(model().attributeHasFieldErrors("hairdressingDTO", "date")).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testInitUpdateHairdressingForm() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID)).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	public void testInitUpdateHairdressingFormAdmin() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testInitUpdateHairdressingFormUnauthorized() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(false);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID)).andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testProcessUpdateHairdressingForm() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2022/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/hairdressings/{hairdressingId}"));
	}

	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	public void testProcessUpdateHairdressingFormAdmin() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2022/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test //Antonio tiene problemas
	public void testProcessUpdateHairdressingFormUnauthorized() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(false);

		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2022/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/errors/accessDenied"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testProcessUpdateHairdressingFormErrors() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(false);

		mockMvc.perform(post("/hairdressings/{hairdressingId}/edit", this.HAIRDRESSING_ID).with(csrf())
				.param("cuidado", "ESTETICA").param("date", "2015/04/04").param("description", "TESTO")
				.param("petName", "Leo").param("time", "6:00")).andExpect(model().attributeHasErrors("hairdressingDTO"))
				.andExpect(model().attributeHasFieldErrors("hairdressingDTO", "date")).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/createOrUpdateHairdressingForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testShowHairdressing() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(get("/hairdressings/{hairdressingId}", this.HAIRDRESSING_ID)).andExpect(status().isOk())
				.andExpect(view().name("hairdressings/hairdressingDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testShowHairdressingInvalid() throws Exception {
		given(this.hairdressingService.findHairdressingById(this.HAIRDRESSING_ID)).willThrow(NoSuchElementException.class);	
		
		mockMvc.perform(get("/hairdressings/{hairdressingId}", this.HAIRDRESSING_ID))
				.andExpect(view().name("errors/elementNotFound")).andExpect(status().isOk());
	}

	@WithMockUser(value = "spring")
	@Test
	public void testShowHairdressingList() throws Exception {
		given(this.hairdressingService.findHairdressingsByUser("spring")).willReturn(Lists.newArrayList(this.hairdressing));
		mockMvc.perform(get("/hairdressings/owner")).andExpect(view().name("hairdressings/hairdressingsList"))
				.andExpect(status().isOk()).andExpect(model().attributeExists("hairdressings"));
	}
	
	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	public void testShowHairdressingListAdmin() throws Exception {
		mockMvc.perform(get("/hairdressings")).andExpect(view().name("hairdressings/hairdressingsList"))
				.andExpect(status().isOk()).andExpect(model().attributeExists("hairdressings"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void testDeleteHairdressingForm() throws Exception {
		given(this.hairdressingService.findHairdressingById(HAIRDRESSING_ID)).willReturn(this.hairdressing);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", this.HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings/owner"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
		given(this.hairdressingService.findHairdressingById(HAIRDRESSING_ID)).willReturn(this.hairdressing);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(false);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().isOk()).andExpect(view().name("errors/accessDenied"));
	}
	
	@WithMockUser(value = "admin", authorities = "admin")
	@Test
	void testProcessDeleteFormAdmin() throws Exception {
		given(this.hairdressingService.findHairdressingById(HAIRDRESSING_ID)).willReturn(this.hairdressing);
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", HAIRDRESSING_ID).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/hairdressings"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormInvalid() throws Exception {
		given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.PET_ID))).willReturn(true);
		given(this.hairdressingService.findHairdressingById(31415)).willThrow(NoSuchElementException.class);

		mockMvc.perform(get("/hairdressings/{hairdressingId}/delete", 31415).with(csrf()))
				.andExpect(status().isOk()).andExpect(view().name("errors/elementNotFound"));
	}
}
