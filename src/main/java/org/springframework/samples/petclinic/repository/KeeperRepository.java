package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Keeper;
import org.springframework.samples.petclinic.model.Vet;

public interface KeeperRepository {
	
	Keeper findById(int id) throws DataAccessException;
	
	Collection<Keeper> findAll() throws DataAccessException;
}
