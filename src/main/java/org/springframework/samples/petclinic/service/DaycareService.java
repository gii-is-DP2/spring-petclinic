package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.repository.springdatajpa.DaycareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DaycareService {
	
	@Autowired
	DaycareRepository daycareRepo;
	
	@Transactional(readOnly=true)
	public Optional<Daycare> findDaycareById(int id) {
		return daycareRepo.findById(id);
	}
	
	public Iterable<Daycare> findDaycaresByPetId(int petId) {
		return daycareRepo.findByPetId(petId);
	}
	
	public void delete(Daycare daycare) {
		daycareRepo.delete(daycare);
	}
	
}
