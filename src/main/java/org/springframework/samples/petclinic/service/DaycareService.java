package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.springdatajpa.DaycareRepository;
import org.springframework.stereotype.Service;

@Service
public class DaycareService {
	@Autowired
	DaycareRepository daycareRepo;
}
