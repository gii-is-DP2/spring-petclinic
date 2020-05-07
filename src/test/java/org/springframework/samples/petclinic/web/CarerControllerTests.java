package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.NoSuchElementException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.CarerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers=CarerController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class CarerControllerTests {

private static final int TEST_CARER_ID = 1;
	
	@MockBean
	private CarerService carerService;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Carer carer;
	
	
	@BeforeEach
	void setup() {
		carer = new Carer();
		carer.setId(this.TEST_CARER_ID);
		carer.setFirstName("Manuel");
		carer.setLastName("Ortiz");
		carer.setDni("47843338");
		carer.setEmail("mob100@gmail.com");
		carer.setSalary(20);
		carer.setTelephone("666925279");
		carer.setIsHairdresser(true);
		
		
		given(this.carerService.findCarerById(TEST_CARER_ID)).willReturn(this.carer);
		
		this.loadAuthContext();
		
	}
	
	private void loadAuthContext() {
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitValidCreationFormAsAdmin() throws Exception {
		mockMvc.perform(get("/carers/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("carer"))
			.andExpect(view().name("carers/createOrUpdateCarerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {		
		mockMvc.perform(post("/carers/new")
				.param("firstName", "Manuel")
				.param("lastName", "Ortiz")
				.with(csrf())
				.param("salary", "20")
				.param("dni", "47843338")
				.param("telephone", "666925279")
				.param("email", "mob100@gmail.com")
				.param("isHairdresser", "true"))
		.andExpect(status().is3xxRedirection());
		
		verify(this.carerService, times(1)).saveCarer(any());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationEmptyFormErrors() throws Exception {
		mockMvc.perform(post("/carers/new").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("carer"))
				.andExpect(model().attributeHasErrors("carer"))
				.andExpect(view().name("carers/createOrUpdateCarerForm"));
	
		verify(this.carerService, times(0)).saveCarer(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(post("/carers/new")
				.with(csrf())
				.param("firstName", "Manuel"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("carer"))
				.andExpect(model().attributeHasFieldErrors("carer", "lastName"))
				.andExpect(model().attributeHasFieldErrors("carer", "dni"))
				.andExpect(model().attributeHasFieldErrors("carer", "telephone"))
				.andExpect(model().attributeHasFieldErrors("carer", "email"))
				.andExpect(model().attributeHasFieldErrors("carer", "isHairdresser"))
				.andExpect(view().name("carers/createOrUpdateCarerForm"));
	
		verify(this.carerService, times(0)).saveCarer(any());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowCarer() throws Exception {
		mockMvc.perform(get("/carers/{carerId}", this.TEST_CARER_ID))
		.andExpect(status().isOk())
		.andExpect(model().attribute("carer", hasProperty("firstName", is(this.carer.getFirstName()))))
		.andExpect(model().attribute("carer", hasProperty("lastName", is(this.carer.getLastName()))))
		.andExpect(model().attribute("carer", hasProperty("dni", is(this.carer.getDni()))))
		.andExpect(model().attribute("carer", hasProperty("telephone", is(this.carer.getTelephone()))))
		.andExpect(model().attribute("carer", hasProperty("salary", is(this.carer.getSalary()))))
		.andExpect(model().attribute("carer", hasProperty("email", is(this.carer.getEmail()))))
		.andExpect(model().attribute("carer", hasProperty("isHairdresser", is(this.carer.getIsHairdresser()))))
		.andExpect(view().name("carers/carerDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowInvalidCarer() throws Exception {
		given(this.carerService.findCarerById(TEST_CARER_ID))
			.willThrow(NoSuchElementException.class);
		
		mockMvc.perform(get("/carers/{carerId}", this.TEST_CARER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAllCarerList() throws Exception {
		given(this.carerService.findCarers()).willReturn(Lists.newArrayList(this.carer));
		
		mockMvc.perform(get("/carer/find"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("carers", iterableWithSize(1)))
			.andExpect(view().name("carers/carersList"));
		
		verify(this.carerService, times(1)).findCarers();
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowAllEmptyTrainersList() throws Exception {
		given(this.carerService.findCarers()).willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/carer/find"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("carers", iterableWithSize(0)))
			.andExpect(view().name("carers/carersList"));
		
		verify(this.carerService, times(1)).findCarers();
		verify(this.carerService, times(0)).findCarersByLastName("");
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitFindForm() throws Exception {
		given(this.carerService.findCarers()).willReturn(Lists.newArrayList(this.carer));
		
		mockMvc.perform(get("/carers"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("carers"))
			.andExpect(view().name("carers/carersList"));
		
		verify(this.carerService, times(1)).findCarers();
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
    	given(this.authorizationService.canUserModifyEmployee(anyString())).willReturn(true);
		
		mockMvc.perform(get("/carers/{carerId}/edit", this.TEST_CARER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("carers/createOrUpdateCarerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
	   	given(this.authorizationService.canUserModifyEmployee(anyString())).willReturn(true);
	
	   	mockMvc.perform(post("/carers/{carerId}/edit", this.TEST_CARER_ID)
	   			.with(csrf())
				.param("email", "fakemail@gmail.com"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("carers/createOrUpdateCarerForm"));
		}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
	   	given(this.authorizationService.canUserModifyEmployee(anyString())).willReturn(true);
		
		mockMvc.perform(post("/carers/{carerId}/edit", this.TEST_CARER_ID)
				.with(csrf())
				.param("lastName", ""))
				.andExpect(model().attributeHasErrors("carer"))
				.andExpect(status().isOk())
				.andExpect(view().name("carers/createOrUpdateCarerForm"));
		}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		given(this.carerService.findCarerById(TEST_CARER_ID)).willReturn(this.carer);
	   	given(this.authorizationService.canUserModifyEmployee(anyString())).willReturn(true);
		
		mockMvc.perform(get("/carers/{carerId}/delete", this.TEST_CARER_ID)
			.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/carers"));
	}
	
}