package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.OwnerValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;

public class OwnerValidatorTests {

	private OwnerValidator ownerValidator;
	private UserService userService;
	private Owner owner;
	
	@BeforeEach
	public void init() {
		this.userService = mock(UserService.class);
		this.ownerValidator = new OwnerValidator(this.userService);
		this.owner = new Owner();
	}
	
	@Test
	public void shouldValidateValid() {
		String username = "fede";
		User user = new User();
		user.setUsername(username);
		user.setPassword("fede");
		user.setEnabled(true);
		this.owner.setUser(user);
		given(this.userService.exists(username)).willReturn(true);
		Errors errors = new BeanPropertyBindingResult(this.owner, "invalidAddress");
		
		this.ownerValidator.validate(this.owner, errors);
		
		assertThat(errors.getErrorCount()).isEqualTo(1);
	}
	
	@Test
	public void shouldValidateInvalid() {
		String username = "fede";
		User user = new User();
		user.setUsername("fede");
		user.setPassword("fede");
		user.setEnabled(true);
		this.owner.setUser(user);
		given(this.userService.exists(username)).willReturn(true);
		Errors errors = new BeanPropertyBindingResult(this.owner, "invalidAddress");
		
		this.ownerValidator.validate(this.owner, errors);
		verify(this.userService, times(1)).exists(username);
		
		assertThat(errors.getErrorCount()).isEqualTo(1);
	}
	
	@Test
	public void shouldValidateNullUser() {
		this.ownerValidator.validate(this.owner, null);
		
		verify(this.userService, times(0)).exists("fede");
		
		Errors errors = new BeanPropertyBindingResult(this.owner, "invalidAddress");
		this.ownerValidator.validate(this.owner, errors);
		
		assertThat(errors.getErrorCount()).isEqualTo(0);
	}
}
