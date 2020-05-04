package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.ServiceType;

public interface ReviewRepository extends CrudRepository<Review, Integer>{

	@Query("SELECT DISTINCT r FROM Review r WHERE r.user.username = :username AND r.serviceType = :serviceType")
	public Collection<Review> findByUsernameAndServiceType(@Param("username") String username, @Param("serviceType") ServiceType serviceType);
	
	@Query("SELECT DISTINCT r FROM Review r WHERE r.id = :id")
	public Review findById(@Param("id") int id);
}
