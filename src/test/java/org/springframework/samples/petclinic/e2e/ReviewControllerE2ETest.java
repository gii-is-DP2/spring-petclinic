package org.springframework.samples.petclinic.e2e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.PetclinicApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerE2ETest {

	public final String TEST_OWNER_USERNAME = "fede"; //Se crea al levantar la BD en memoria con data.sql
	public final String TEST_ADMIN_USERNAME = "admin"; //Idem

	@Autowired
	private MockMvc mockMvc;

    @Test
	void testNotLoggedIn() throws Exception { //La annotation @IsAuthenticated esta sobre el nombre de la clase
    	//Por ende pruebo en un solo metodo lo que sucede si el usuario no esta loggeado
		mockMvc.perform(get("/reviews/new"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/login"));
	}
    
	@WithMockUser(username = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/reviews/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("reviewDTO"))
			.andExpect(view().name("reviews/createReviewForm"));
	}
	
	@WithMockUser(username = TEST_ADMIN_USERNAME, authorities = {"admin"})
    @Test
	void testInitCreationFormAdmin() throws Exception {
		mockMvc.perform(get("/reviews/new"))
			.andExpect(status().isForbidden());
	}

    @WithMockUser(value = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/reviews"));
	}
    
    @WithMockUser(value = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessCreationFormAdminForbidden() throws Exception {
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
			.andExpect(status().isForbidden());
	}
    
    @WithMockUser(value = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormMissingFields() throws Exception {
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("reviewDTO"))
			.andExpect(model().attributeHasFieldErrors("reviewDTO", "serviceType"))
			.andExpect(model().attributeHasFieldErrors("reviewDTO", "rating"))
			.andExpect(view().name("reviews/createReviewForm"));
	}
    
    @WithMockUser(value = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessCreationFormRepeatedServiceType() throws Exception {
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/reviews"));
		
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Otros comentarios")
				.param("serviceType", "DAYCARE") //Al crear un segundo review de mismo tipo de servicio deberia devolver error
				.param("rating", "2")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("reviewDTO"))
			.andExpect(view().name("reviews/createReviewForm"));
	}
    
    @WithMockUser(value = TEST_OWNER_USERNAME, authorities = {"owner"})
	@Test
	void testProcessDeleteFormForbidden() throws Exception {
		mockMvc.perform(get("/reviews/{reviewId}/delete", 4) //Se crea al levantar la BD en memoria
				.with(csrf()))
			.andExpect(status().isForbidden());
	}
    
    @WithMockUser(value = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		mockMvc.perform(get("/reviews/{reviewId}/delete", 4) 
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/reviews"));
	}
	
    @WithMockUser(value = TEST_ADMIN_USERNAME, authorities = {"admin"})
	@Test
	void testProcessDeleteFormInvalidReview() throws Exception {    	
		mockMvc.perform(get("/reviews/{reviewId}/delete", 99)) //Review con ID 99 no existe
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
    
    @WithMockUser(value = TEST_OWNER_USERNAME, authorities = {"owner"})
    @Test
	void testShowReviewList() throws Exception {
		mockMvc.perform(get("/reviews"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("reviews"))
				.andExpect(view().name("reviews/reviewList"));
	}
	
}
