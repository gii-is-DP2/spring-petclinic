package org.springframework.samples.petclinic.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Keeper;
import org.springframework.samples.petclinic.repository.KeeperRepository;
import org.springframework.stereotype.Service;

@Service
public class KeeperService {

	private KeeperRepository keeperRepository;
	
	@Autowired
	public KeeperService(KeeperRepository keeperRepository) {
		this.keeperRepository = keeperRepository;
	}
	
	@Transactional(readOnly = true)
	public Keeper findKeeperById(int id) throws DataAccessException {
		return this.keeperRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Keeper> getAllKeepers() throws DataAccessException {
		return this.keeperRepository.findAll();
	}
}
