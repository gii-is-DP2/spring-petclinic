
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Client;
import org.springframework.samples.petclinic.model.UserPet;
import org.springframework.samples.petclinic.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;


	@Transactional
	public int clientCount() {
		return (int) this.clientRepository.count();
	}
	
	@Transactional
	public void saveClient(Client client) throws DataAccessException {
		//creating owner
		clientRepository.save(client);		
		//creating user
		UserPet user = new UserPet();
		user.setPassword(client.getPassword());
		user.setUsername(client.getEmail());
		user.setEnabled(true);
		userService.saveUser(user);
		//creating authorities
		authoritiesService.saveAuthorities(client.getEmail(), "client");
	}		
}
