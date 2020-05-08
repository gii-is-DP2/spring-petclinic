package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.ServiceType;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.util.TrainingDTO;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers=ReviewController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class ReviewControllerTests {
	
	public final String USERNAME = "spring";
	
	@MockBean
	private ReviewService reviewService;
	
	@MockBean
	private UserService userService;

	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private Authentication auth;
	
	@MockBean
	private SecurityContext securityContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private User user;
	private Review review;
	
	@BeforeEach
	void setup() {
		
		user = new User();
		user.setUsername("spring");
		user.setEnabled(true);
		
		review = new Review();
		review.setComments("Comentarios!");
		review.setDate(LocalDate.now());
		review.setId(1);
		review.setRating(5);
		review.setUser(user);
		review.setServiceType(ServiceType.HAIRDRESSING);
		
		given(securityContext.getAuthentication()).willReturn(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@WithMockUser(value = USERNAME)
    @Test
	void testShowReviewList() throws Exception {
		given(reviewService.findReviews()).willReturn(Lists.newArrayList(review));
		mockMvc.perform(get("/reviews"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("reviews"))
				.andExpect(view().name("reviews/reviewList"));
	}
	
	@WithMockUser(value = USERNAME)
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/reviews/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("reviewDTO"))
			.andExpect(view().name("reviews/createReviewForm"));
	}
	
	@WithMockUser(value = USERNAME)
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		given(userService.findByUsername(USERNAME)).willReturn(user);

		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
		.andExpect(status().is3xxRedirection());
		
		verify(reviewService, times(1)).saveReview(any());
	}
	
	@WithMockUser(value = USERNAME)
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
		
		verify(reviewService, times(0)).saveReview(any());
	}
	
	@WithMockUser(value = USERNAME)
	@Test
	void testProcessCreationFormSaveErrors() throws Exception {
		given(userService.findByUsername(USERNAME)).willReturn(user);

		doThrow(BusinessException.class).when(reviewService).saveReview(any());
		
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("reviewDTO"))
			.andExpect(view().name("reviews/createReviewForm"));
	
		verify(reviewService, times(1)).saveReview(any());
	}
	
	@WithMockUser(value = USERNAME)
	@Test
	void testProcessCreationFormInvalidUser() throws Exception {
		given(userService.findByUsername(USERNAME)).willThrow(new DataAccessException("...") {});
		
		mockMvc.perform(post("/reviews/new")
				.param("comments", "Comentarios")
				.param("serviceType", "DAYCARE")
				.param("rating", "3")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	
		verify(reviewService, times(0)).saveReview(any());
	}
	
	@WithMockUser(value = USERNAME)
	@Test
	void testProcessDeleteFormSuccess() throws Exception {
		given(reviewService.findReviewById(review.getId())).willReturn(review);
    	
		mockMvc.perform(get("/reviews/{reviewId}/delete", review.getId())
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/reviews"));

		verify(reviewService, times(1)).deleteReview(any());
	}
	
	@WithMockUser(value = USERNAME)
	@Test
	void testProcessDeleteFormInvalidReview() throws Exception {
		given(reviewService.findReviewById(review.getId())).willThrow(NoSuchElementException.class);
    	
		mockMvc.perform(get("/reviews/{reviewId}/delete", review.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("errors/elementNotFound"));
	}
	
}
