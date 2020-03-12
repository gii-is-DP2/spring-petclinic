package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.springdatajpa.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	@Autowired 
	private ReviewRepository reviewRepo;

}
