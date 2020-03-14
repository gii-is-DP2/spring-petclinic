package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
	@Autowired
	private TrainerRepository trainerRepository;
}
