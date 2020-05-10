package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationService {

	private PetService petService;
	
	private final OwnerService ownerService;
		
	@Autowired
	public AuthorizationService(PetService petService, OwnerService ownerService) {
		this.petService = petService;
		this.ownerService = ownerService;
		
	}
	

	@Transactional(readOnly = true)
	public boolean canUserModifyEmployee(String userId) {
		
		if(userId.equals("admin")) {
			return true;
		} else {
			return false;
		}
	}
		
	@Transactional(readOnly = true)
	public boolean canUserModifyBooking(String userId, int petId) {
		Pet pet = this.petService.findPetById(petId);
		return userId.equals("admin") || pet.getOwner().getUser().getUsername().equals(userId);
	}
	
	@Transactional(readOnly = true)
	public boolean canUserModifyHisData(String userId, int ownerId) {
		Owner owner = this.ownerService.findOwnerByUsername(userId);
		return userId.equals("admin") || owner.getId().equals(ownerId);
	}
}
