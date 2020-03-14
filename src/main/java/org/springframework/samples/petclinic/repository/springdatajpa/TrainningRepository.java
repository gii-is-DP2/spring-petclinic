package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Trainning;

public interface TrainningRepository extends CrudRepository<Trainning, Integer>{

}
