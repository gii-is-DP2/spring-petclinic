/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.ServiceType;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.service.exceptions.MappingException;
import org.springframework.samples.petclinic.util.ReviewDTO;
import org.springframework.samples.petclinic.web.annotations.IsAuthenticated;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */

@IsAuthenticated
@Controller
public class ReviewController {

	private final ReviewService reviewService;
	private final UserService userService;

	private static final String VIEWS_REVIEW_CREATE_FORM = "reviews/createReviewForm";
	private static final String VIEWS_REVIEW_LIST = "reviews/reviewList";

	@Autowired
	public ReviewController(ReviewService reviewService, UserService userService) {
		this.reviewService = reviewService;
		this.userService = userService;
	}

	@ModelAttribute("serviceTypes")
	public Collection<ServiceType> populateServiceTypes() {
		ServiceType[] types = ServiceType.class.getEnumConstants();
		return Arrays.asList(types);
	}
	
	@ModelAttribute("ratings")
	public Collection<Integer> populateRatings() {
		return Arrays.asList(new Integer[] {1,2,3,4,5});
	}

	@GetMapping(value = { "/reviews" })
	public String showReviewList(Map<String, Object> model) {
		Collection<Review> reviews = this.reviewService.findReviews();
		model.put("reviews", reviews);
		return VIEWS_REVIEW_LIST;
	}

	@GetMapping(value = "/reviews/new")
	public String initReviewCreationForm(Map<String, Object> model) {
		ReviewDTO dto = new ReviewDTO();
		model.put("reviewDTO", dto);
		return VIEWS_REVIEW_CREATE_FORM;
	}
	
	@PostMapping(value = "/reviews/new")
	public String processCreationForm(@Valid ReviewDTO reviewDTO, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_REVIEW_CREATE_FORM;
		}
		else {
			Review review;
			try {
				review = this.convertToEntity(reviewDTO);
			} catch (MappingException ex) {
	            return "errors/elementNotFound";
			}
			try {
				this.reviewService.saveReview(review);
			} catch (BusinessException ex) {
				result.rejectValue(ex.getField(), ex.getCode(), ex.getMessage());
				return VIEWS_REVIEW_CREATE_FORM;
			}
			return "redirect:/reviews";
		}
	}
	
	@GetMapping(value = "/reviews/{reviewId}/delete")
	public String processDeleteReviewForm(@PathVariable("reviewId") int reviewId) {
		Review review = this.reviewService.findReviewById(reviewId);
		this.reviewService.deleteReview(review);
		return "redirect:/reviews";
	}
	
	private Review convertToEntity(ReviewDTO dto) throws MappingException {
		Review review = new Review();
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = this.userService.findByUsername(username);
			review.setUser(user);
		} catch(DataAccessException e) {
			throw new MappingException("reviewDTO", "Not existance", "User does not exist");
		}
		review.setRating(dto.getRating());
		review.setComments(dto.getComments());
		review.setServiceType(dto.getServiceType());
		review.setDate(LocalDate.now());
		
		return review;
	}

}
