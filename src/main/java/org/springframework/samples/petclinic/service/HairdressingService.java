package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.HairdressingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HairdressingService {
	@Autowired
	HairdressingRepository hairdressingRepo;
	@Autowired
	PetRepository petRepo;
	
	@Transactional
	public Iterable<Hairdressing> findAll() {
		return hairdressingRepo.findAll();
	}
	
	@Transactional(readOnly=true)
	public Optional<Hairdressing> findHairdressingById(int id){
		return hairdressingRepo.findById(id);
	}
	
	@Transactional(readOnly=true)
	public Pet findPetById(int id){
		return petRepo.findById(id);
	}
	
	@Transactional
	public void save(Hairdressing hairdressing) {
		hairdressingRepo.save(hairdressing);
	}
	
	@Transactional
	public void delete(Hairdressing hairdressing) {
		hairdressingRepo.delete(hairdressing);
	}
	
}
