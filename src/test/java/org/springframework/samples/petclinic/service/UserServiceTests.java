package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class UserServiceTests {

	@Autowired
	private UserService realUserService;
	private UserService userService;
	private UserRepository userRepository;
	
	@BeforeEach
	public void initMocks() {
		this.userRepository = mock(UserRepository.class);
		this.userService = new UserService(this.userRepository);
	}
	
	@Test
	public void shouldExistUser() {
		String username = "fedesartu";
		when(this.userRepository.existsById(username)).thenReturn(true);
		
		Boolean exists = this.userService.exists(username);
		
		verify(this.userRepository, times(1)).existsById(username);
		assertThat(exists).isTrue();
	}
	
	@Test
	@Transactional
	public void shouldInsertUser() {
		String username = "fedesartu";
		Boolean existsBefore = this.realUserService.exists(username);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword("pass");
		user.setEnabled(true);
		
		this.realUserService.saveUser(user);
		
		Boolean existsAfter = this.realUserService.exists(username);
		assertThat(existsBefore).isFalse();
		assertThat(existsAfter).isTrue();
	}
}
