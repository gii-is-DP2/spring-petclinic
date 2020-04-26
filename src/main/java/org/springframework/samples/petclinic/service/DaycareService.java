package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.springdatajpa.DaycareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DaycareService {
	
	private final DaycareRepository daycareRepository;
	
	@Autowired
	public DaycareService(DaycareRepository daycareRepository) {
		this.daycareRepository = daycareRepository;
	}

	@Transactional(readOnly = true)
	public Daycare findDaycareById(int id) throws DataAccessException {
		return this.daycareRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public Collection<Daycare> findDaycares() throws DataAccessException {
		return (Collection<Daycare>) this.daycareRepository.findAll();
	}
	
	@Transactional
	public void saveDaycare(Daycare daycare) throws DataAccessException {
		this.daycareRepository.save(daycare);
	}
  
	@Transactional
	public void delete(int daycareId) throws DataAccessException{
		Daycare daycare = this.findDaycareById(daycareId);
		Pet pet = daycare.getPet();
		pet.deleteDaycare(daycare.getId());
		this.daycareRepository.deleteById(daycareId);
	}
  
	@Transactional
  	public Collection<Daycare> findDaycaresByPetId(int petId) {
		return daycareRepository.findByPetId(petId);
	}
	
	@Transactional(readOnly = true)
	public Integer countDaycareByDate(LocalDate localDate) throws DataAccessException {
		return this.daycareRepository.countDaycareByDate(localDate);
	}
	
	@Transactional(readOnly = true)
	public Integer oneDaycareById(LocalDate localDate, Integer id) throws DataAccessException {
		return this.daycareRepository.countDaycareByDateAndPetId(localDate, id);
	}

	@Transactional(readOnly = true)
	public Collection<Daycare> findDaycaresByUser(String user) throws DataAccessException {
		return (Collection<Daycare>) this.daycareRepository.findByUser(user);
	}
}
