package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.repository.springdatajpa.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
	@Autowired 
	private ReviewRepository reviewRepository;
	
	@Autowired
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}	
	
	@Transactional
	public void saveReview(Review review) throws DataAccessException {
		reviewRepository.save(review);		
	}	
	
	@Transactional(readOnly = true)	
	public Collection<Review> findReviews() throws DataAccessException {
		return (Collection<Review>) reviewRepository.findAll();
	}	
}
