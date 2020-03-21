package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.repository.springdatajpa.DaycareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DaycareService {
	
	@Autowired
	DaycareRepository daycareRepository;
	

	@Transactional(readOnly = true)
	public Daycare findDaycareById(int id) throws DataAccessException {
		return this.daycareRepository.findById(id).get();
	}
	
	@Transactional
	public void saveDaycare(Daycare daycare) throws DataAccessException {
		this.daycareRepository.save(daycare);		
	}
  
  public void delete(Daycare daycare) {
		daycareRepository.delete(daycare);
	}
  
  	public Iterable<Daycare> findDaycaresByPetId(int petId) {
		return daycareRepository.findByPetId(petId);
	}
}
