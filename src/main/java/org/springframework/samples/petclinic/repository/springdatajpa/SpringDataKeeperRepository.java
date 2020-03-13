package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Keeper;
import org.springframework.samples.petclinic.repository.KeeperRepository;


public interface SpringDataKeeperRepository extends KeeperRepository, Repository<Keeper, Integer> {

	@Override
	@Query("SELECT keeper FROM Keeper keeper")
	public Keeper findById(@Param("id") int id);
}
