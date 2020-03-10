
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {

}
