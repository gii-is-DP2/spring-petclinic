package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.HairdressingRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HairdressingService {
	
	private final HairdressingRepository hairdressingRepo;
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
	public Hairdressing findHairdressingById(int id) throws DataAccessException{
		return hairdressingRepo.findById(id).get();
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
		Hairdressing hairdressing = this.findHairdressingById(hairdressingId);
		Pet pet = hairdressing.getPet();
		pet.deleteHairdressing(hairdressing.getId());
		hairdressingRepo.deleteById(hairdressingId);
	}

	public int countHairdressingsByDateAndTime(LocalDate date, String time) {
		return hairdressingRepo.countHairdressingsByDateAndTime(date, time);
	}

	
	
}
