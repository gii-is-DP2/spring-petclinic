package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationService {

	private PetService petService;
	
	@Autowired
	public AuthorizationService(PetService petService) {
		this.petService = petService;
	}
	
	@Transactional(readOnly = true)
	public boolean canUserModifyBooking(String userId, int petId) {
		Pet pet = this.petService.findPetById(petId);
		return userId.equals("admin") || pet.getOwner().getUser().getUsername().equals(userId);
	}
}
