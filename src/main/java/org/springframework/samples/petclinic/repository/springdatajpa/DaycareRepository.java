package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Daycare;

public interface DaycareRepository extends CrudRepository<Daycare, Integer> {

}
