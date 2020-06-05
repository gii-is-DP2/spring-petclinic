package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.ServiceType;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.springdatajpa.ReviewRepository;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
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
	
	@Transactional(readOnly = true)
	public Review findReviewById(int id) throws DataAccessException {
		return reviewRepository.findById(id).get();
	}
	
	@Transactional
	public void saveReview(Review review) throws DataAccessException, BusinessException {
		ServiceType type = review.getServiceType();
		User user = review.getUser();
		if(this.existsByUserAndServiceType(user, type)) {
			throw new BusinessException("serviceType", "serviceType", "You have already submitted a review for this service.");
		}
		reviewRepository.save(review);
	}	
	
	@Transactional(readOnly = true)	
	public Collection<Review> findReviews() throws DataAccessException {
		return (Collection<Review>) reviewRepository.findAll();
	}	

	@Transactional(readOnly = true)	
	public boolean existsByUserAndServiceType(User user, ServiceType type) {
		Collection<Review> found = this.reviewRepository.findByUsernameAndServiceType(user.getUsername(), type);
		return found.size() > 1;
	}
	@Transactional
	public void deleteReview(Review review) throws DataAccessException {
		this.reviewRepository.delete(review);
	}
}
