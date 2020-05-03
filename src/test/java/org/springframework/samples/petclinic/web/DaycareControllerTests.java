package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.DaycareService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.collect.Lists;


@WebMvcTest(controllers = DaycareController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class DaycareControllerTests {

	private static final int TEST_DAYCARE_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_OWNER_ID = 20;

	@MockBean
	private DaycareService daycareService;
	
	@MockBean
	private PetService petService;
	
	@Autowired
	private DaycareController daycareController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	private Daycare daycare;
	private Pet pet;
	
	@BeforeEach
	void setup() {
		daycare = new Daycare();
		daycare.setId(this.TEST_DAYCARE_ID);
		daycare.setDate(LocalDate.now());
		daycare.setDescription("Descripcion");
		pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.addDaycare(daycare);
		daycare.setPet(pet);
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.addPet(pet);
		given(this.daycareService.findDaycareById(TEST_DAYCARE_ID)).willReturn(this.daycare);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(this.pet);
		
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}

        @WithMockUser(value = "spring")
        @Test
	void testInitCreationForm() throws Exception {
        	mockMvc.perform(get("/daycares/new"))
        	.andExpect(status().isOk())
			.andExpect(model().attributeExists("daycareDTO"))
			.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/daycares/new")
				.param("description", "asdasd")
				.param("date", "2020/10/10")
				.param("petName", "fede")
				.param("capacity", "5")
				.with(csrf()))
                .andExpect(status().is2xxSuccessful());
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/daycares/new")
				.param("description", "asdasd")
				.param("date", "2015/02/12")
							.with(csrf()))
				.andExpect(model().attributeHasErrors("daycareDTO"))
				.andExpect(status().isOk())
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
		
		mockMvc.perform(get("/daycares/{daycareId}/edit", this.TEST_DAYCARE_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
	   	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
	
	   	mockMvc.perform(post("/daycares/{daycareId}/edit", this.TEST_DAYCARE_ID)
	   			.with(csrf())
				.param("description", "asdasda")
				.param("date", "2020/10/10"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
		}
	 
	 @WithMockUser(value = "spring")
		@Test
		void testInitUpdateFormUnauthorized() throws Exception {
	    	
	    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(false);
	    	
			mockMvc.perform(get("/daycares/{daycareId}/edit", this.TEST_DAYCARE_ID))
					.andExpect(status().isOk())
					.andExpect(view().name("errors/accessDenied"));
		}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
	   	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
		
		mockMvc.perform(post("/daycares/{daycareId}/edit", this.TEST_DAYCARE_ID)
				.with(csrf())
				.param("description", "asdasda")
				.param("date", "2015/02/12"))
				.andExpect(model().attributeHasErrors("daycareDTO"))
				.andExpect(status().isOk())
				.andExpect(view().name("daycares/createOrUpdateDaycareForm"));
		}
	 	 
	@WithMockUser(value = "spring")
    @Test
	void testShowDaycare() throws Exception {
	   	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
		
		mockMvc.perform(get("/daycares/{daycareId}", TEST_DAYCARE_ID))
		.andExpect(status().isOk())
		.andExpect(view().name("daycares/daycareDetails"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testShowDaycares() throws Exception {
	   	mockMvc.perform(get("/daycares"))
	   		.andExpect(status().isOk())
	   		.andExpect(model().attributeExists("daycares"))
	   		.andExpect(view().name("daycares/daycaresList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testShowOwnerDaycares() throws Exception {
		given(this.daycareService.findDaycaresByUser("spring")).willReturn(Lists.newArrayList(this.daycare));
		mockMvc.perform(get("/trainings/owner"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("daycares")).andExpect(view().name("daycares/daycaresList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		given(this.daycareService.findDaycareById(TEST_DAYCARE_ID)).willReturn(this.daycare);
	   	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(true);
		
		mockMvc.perform(get( "/daycares/{daycareId}/delete", this.TEST_DAYCARE_ID)
			.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/daycares/owner"));
	}
	
	@WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testProcessDeleteFormUnauthorized() throws Exception {
    	
    	given(this.authorizationService.canUserModifyBooking(anyString(), eq(this.TEST_PET_ID))).willReturn(false);
    	
		mockMvc.perform(get("/daycares/{daycareId}/delete", TEST_DAYCARE_ID)
							.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("errors/accessDenied"));
	}
	
}
	
		
		

