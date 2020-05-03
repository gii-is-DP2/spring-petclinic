package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OwnerValidator implements Validator {

	private final UserService userService;
	
	public OwnerValidator(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Owner.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Owner owner = (Owner) target;
		User user = (User) owner.getUser();
		
		if (user != null && owner.getId() != null) {
			String username = owner.getUser().getUsername();
			
			if (this.userService.exists(username)) {
				errors.rejectValue("user.username", "unique", "There is already a owner with same username.");
			}
		}
	}

}
