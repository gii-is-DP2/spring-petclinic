package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class UserServiceTests {

	@Autowired
	private UserService userService;
	
	private User createUser(String username) {
		User user = new User();
		user.setUsername(username);
		user.setPassword("pass");
		user.setEnabled(true);
		
		return user;
	}
	
	@Test
	public void shouldExistUser() {
		String username = "fedesartu";
		User user = this.createUser(username);
		this.userService.saveUser(user);
		
		
		Boolean exists = this.userService.exists(username);
		
		assertThat(exists).isTrue();
	}
	
	@Test
	public void shouldNotExistUser() {
		Boolean exists = this.userService.exists("fedes");
		
		assertThat(exists).isFalse();
	}
	
	@Test
	@Transactional
	public void shouldInsertUser() {
		String username = "fedesartu";
		Boolean existsBefore = this.userService.exists(username);
		
		User user = this.createUser(username);
		
		this.userService.saveUser(user);
		
		Boolean existsAfter = this.userService.exists(username);
		assertThat(existsBefore).isFalse();
		assertThat(existsAfter).isTrue();
	}
	
	@Test
	@Transactional
	public void shouldNotInsertUser() {
		User user = this.createUser("fedes");
		user.setUsername(null);
		
		Assertions.assertThrows(DataAccessException.class, () -> {
			this.userService.saveUser(user);
		});
	}
	
	@Test
	@Transactional
	public void shouldFindByUsername() {
		String username = "fedesartu";
		
		User user = this.createUser(username);
		
		this.userService.saveUser(user);
		
		User userFound = this.userService.findByUsername(username);
		assertThat(userFound).isNotNull();
		assertThat(userFound.getUsername()).isEqualTo(username);
	}
	
	@Test
	@Transactional
	public void shouldNotFindByUsername() {
		String username = "fedesartu";
		
		User userFound = this.userService.findByUsername(username);
		assertThat(userFound).isNull();
	}
}
