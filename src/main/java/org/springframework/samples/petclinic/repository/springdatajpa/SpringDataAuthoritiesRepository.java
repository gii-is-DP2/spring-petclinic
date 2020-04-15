package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.repository.AuthoritiesRepository;

public interface SpringDataAuthoritiesRepository extends AuthoritiesRepository, CrudRepository<Authorities, String>{

	@Override
	@Query("SELECT auth FROM Authorities auth WHERE auth.authority =:authority")
	public Authorities findByAuthority(@Param("authority") String authority);
}
