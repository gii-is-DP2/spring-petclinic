package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.ServiceType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewServiceTest {
	
	private final String USER_USERNAME = "fede";
	private final int REVIEW_ID = 1;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;
	
	private Review review;
	
	private int addReview() throws DataAccessException, BusinessException {
		review = new Review();
		review.setComments("Comentarios");
		review.setDate(LocalDate.now());
		review.setServiceType(ServiceType.TRAINING);
		review.setRating(4);
		review.setUser(userService.findByUsername(this.USER_USERNAME));
		
		reviewService.saveReview(review);
				
		return review.getId();
	}
	
	@Test
	@Transactional
	public void shouldInsertReview() throws DataAccessException, BusinessException {
		this.addReview();
		int previousReview = this.reviewService.findReviews().size();
		Review newReview = new Review();
		newReview.setComments("Very Good");
		newReview.setDate(LocalDate.now().plusDays(2));
		newReview.setRating(3);
		newReview.setServiceType(ServiceType.HAIRDRESSING); 
		newReview.setUser(userService.findByUsername(this.USER_USERNAME));
		
		this.reviewService.saveReview(newReview);
		
		Collection<Review> actualReviews = this.reviewService.findReviews();
		assertThat(actualReviews.size()).isEqualTo(previousReview + 1);
	}
	
	@Test
	@Transactional
	public void shouldFindReviewById() throws DataAccessException, BusinessException {
		int id = this.addReview();

		Review foundReview = this.reviewService.findReviewById(id);
		
		assertThat(foundReview).isNotNull();
		assertThat(foundReview.getDate()).isEqualTo(review.getDate());
		assertThat(foundReview.getComments()).isEqualTo(review.getComments());
		assertThat(foundReview.getRating()).isEqualTo(review.getRating());
		assertThat(foundReview.getServiceType()).isEqualTo(review.getServiceType());
		assertThat(foundReview.getId()).isEqualTo(id);
		assertThat(foundReview.getUser().getUsername()).isEqualTo(review.getUser().getUsername());
	}
	
	@Test
	@Transactional
	void shouldNotFindReviewById() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			reviewService.findReviewById(99);
		});
	}

	@Test
	@Transactional
	void shouldFindReviews() throws DataAccessException, BusinessException {
		Collection<Review> foundReviews = reviewService.findReviews();
		int previousSize = foundReviews.size();
		this.addReview();
		foundReviews = reviewService.findReviews();
		assertThat(foundReviews.size()).isEqualTo(previousSize + 1);
		assertThat(foundReviews).contains(review);
	}
	
	@Test
	@Transactional
	void shouldFindByUsernameAndServiceType() throws BusinessException {
		addReview();
		boolean exists = reviewService.existsByUserAndServiceType(review.getUser(), review.getServiceType());
		assertThat(exists).isTrue();
	}
	
	@Test
	@Transactional
	void shouldNotFindByUsernameAndServiceType() throws BusinessException {
		boolean exists = reviewService.existsByUserAndServiceType(userService.findByUsername(USER_USERNAME), ServiceType.DAYCARE);
		assertThat(exists).isFalse();
	}
	
	@Test
	@Transactional
	void shouldNotSaveReviewOfSameUserAndServiceType() throws BusinessException {
		addReview();
		Review second = new Review();
		second.setComments("Comentariossss");
		second.setDate(LocalDate.now());
		second.setUser(review.getUser());
		second.setServiceType(review.getServiceType());
		second.setRating(2);
		Assertions.assertThrows(BusinessException.class, () -> {
			reviewService.saveReview(second);
		});
	}
	
	@Test
	@Transactional
	public void shouldDeleteReview() throws DataAccessException, BusinessException {
		addReview();
		int previousReviews = reviewService.findReviews().size();
		
		reviewService.deleteReview(review);
		
		Collection<Review> actualReviews = reviewService.findReviews();
		assertThat(actualReviews.size()).isEqualTo(previousReviews - 1);
		assertThat(actualReviews).doesNotContain(review);
	}
	
}
