package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers=OwnerController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private OwnerController ownerController;

	@MockBean
	private OwnerService ownerService;
        
    @MockBean
	private UserService userService;
        
    @MockBean
    private AuthoritiesService authoritiesService;
    
    @MockBean
    private AuthorizationService authorizationService;
    
    @MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;

	@Autowired
	private MockMvc mockMvc;

	private Owner george;

	@BeforeEach
	void setup() {

		george = new Owner();
		george.setId(TEST_OWNER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(george);

		this.loadAuthContext();
	}
	
	private void loadAuthContext() {
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}

	@WithMockUser(value = "spring")
        @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/new").param("firstName", "Joe").param("lastName", "Bloggs")
							.with(csrf())
							.param("address", "123 Caramel Street")
							.param("city", "London")
							.param("telephone", "01316761638"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/new")
							.with(csrf())
							.param("firstName", "Joe")
							.param("lastName", "Bloggs")
							.param("city", "London"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessFindFormSuccess() throws Exception {
		given(this.ownerService.findOwnerByLastName("")).willReturn(Lists.newArrayList(george, new Owner()));

		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(view().name("owners/ownersList"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessFindFormByLastName() throws Exception {
		given(this.ownerService.findOwnerByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));

		mockMvc.perform(get("/owners").param("lastName", "Franklin")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}

        @WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		mockMvc.perform(get("/owners").param("lastName", "Unknown Surname")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
				.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
				.andExpect(view().name("owners/findOwners"));
	}

        @WithMockUser(value = "spring")
	@Test
	void testInitUpdateOwnerForm() throws Exception {
    		given(this.authorizationService.canUserModifyHisData(anyString(), eq(this.TEST_OWNER_ID))).willReturn(true);

        	mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

        @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
    		given(this.authorizationService.canUserModifyHisData(anyString(), eq(this.TEST_OWNER_ID))).willReturn(true);

        	mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
							.with(csrf())
							.param("firstName", "Joe")
							.param("lastName", "Bloggs")
							.param("address", "123 Caramel Street")
							.param("city", "London")
							.param("telephone", "01616291589"))
				.andExpect(status().is3xxRedirection());
	}

        @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
    		given(this.authorizationService.canUserModifyHisData(anyString(), eq(this.TEST_OWNER_ID))).willReturn(true);

        	
        	mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
							.with(csrf())
							.param("firstName", "Joe")
							.param("lastName", "Bloggs")
							.param("city", "London"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

        @WithMockUser(value = "spring")
	@Test
	void testShowOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
				.andExpect(view().name("owners/ownerDetails"));
	}
        
    @WithMockUser(value = "spring")
    @Test
    void shouldShowProfile() throws Exception {
    	given(this.ownerService.findOwnerByUsername(any()))
    		.willReturn(george);
    	
    	mockMvc.perform(get("/profile"))
    	.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("owners/profile"));
    }

}
