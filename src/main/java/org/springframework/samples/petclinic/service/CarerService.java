package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.repository.CarerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarerService {
	
	private final CarerRepository carerRepository;
	
	@Autowired
	public CarerService(CarerRepository carerRepository) {
		this.carerRepository = carerRepository;
	}
	
	@Transactional(readOnly = true)
	public Carer findCarerById(int id) throws DataAccessException {
		return this.carerRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	@Cacheable("allCarers")
	public Collection<Carer> findCarers() throws DataAccessException {
		return (Collection<Carer>) this.carerRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Collection<Carer> findCarersByLastName(String lastName) throws DataAccessException {
		return (Collection<Carer>) this.carerRepository.findByLastName(lastName);
	}
	
	@Transactional
	@CacheEvict(cacheNames="allCarers", allEntries=true)
	public void saveCarer(Carer carer) throws DataAccessException {
		this.carerRepository.save(carer);		
	}
	
	@Transactional
	@CacheEvict(cacheNames="allCarers", allEntries=true)
	public void delete(int carerId) throws DataAccessException{
		this.carerRepository.deleteById(carerId);
	}

}
