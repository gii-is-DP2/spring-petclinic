package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.HairdressingRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HairdressingService {
	
	HairdressingRepository hairdressingRepo;
	@Autowired
	PetRepository petRepo;
	
	@Autowired
	public HairdressingService(HairdressingRepository hairdressingRepository) {
		this.hairdressingRepo = hairdressingRepository;
	}	
	
	@Transactional
	public Iterable<Hairdressing> findAll() {
		return hairdressingRepo.findAll();
	}
	
	@Transactional(readOnly=true)
	public Optional<Hairdressing> findHairdressingById(int id){
		return hairdressingRepo.findById(id);
	}
	
	@Transactional(readOnly=true)
	public Pet findByPetId(int id){
		return petRepo.findById(id);
	}
	
	@Transactional
	public void save(Hairdressing hairdressing) {
		hairdressingRepo.save(hairdressing);
	}
	
	@Transactional
	public void delete(int hairdressingId) {
		Hairdressing hairdressing = this.findHairdressingById(hairdressingId).get();
		Pet pet = hairdressing.getPet();
		pet.deleteHairdressing(hairdressing.getId());
		hairdressingRepo.deleteById(hairdressingId);
	}

	public int countHairdressingsByDateAndTime(LocalDate date, String time) {
		return hairdressingRepo.countHairdressingsByDateAndTime(date, time);
	}

	
	
}
