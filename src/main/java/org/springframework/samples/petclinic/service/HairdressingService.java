package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.springdatajpa.HairdressingRepository;
import org.springframework.stereotype.Service;

@Service
public class HairdressingService {
	@Autowired
	HairdressingRepository HairdressingRepo;
}
