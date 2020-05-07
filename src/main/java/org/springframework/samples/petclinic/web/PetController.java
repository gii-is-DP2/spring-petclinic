/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Collection;
import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final PetService petService;
    private final OwnerService ownerService;
    private final AuthorizationService authorizationService;

	@Autowired
	public PetController(PetService petService, OwnerService ownerService, AuthorizationService authorizationService) {
		this.petService = petService;
        this.ownerService = ownerService;
        this.authorizationService = authorizationService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.ownerService.findOwnerById(ownerId);
	}
	
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping(value = "/pets/new")
	public String initCreationForm(@PathVariable("ownerId") int ownerId, ModelMap model) {
		authorizeUserAction(ownerId);
		Owner owner = ownerService.findOwnerById(ownerId);
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pets/new")
	public String processCreationForm(@PathVariable("ownerId") int ownerId, @Valid Pet pet, BindingResult result, ModelMap model) {	
		authorizeUserAction(ownerId);
		Owner owner = ownerService.findOwnerById(ownerId);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
                    try{
                    	owner.addPet(pet);
                    	this.petService.savePet(pet);
                    }catch(DuplicatedPetNameException ex){
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
                    }
                    return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,  ModelMap model) {
		this.authorizeUserAction(ownerId);
		Pet pet = this.petService.findPetById(petId);
		this.authorizeUserActionOnPet(ownerId, pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}
	
    @PostMapping(value = "/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner,@PathVariable("petId") int petId, @PathVariable("ownerId") int ownerId, ModelMap model) {
        this.authorizeUserAction(ownerId);
        	
        if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
            Pet petToUpdate=this.petService.findPetById(petId);
    		this.authorizeUserActionOnPet(ownerId, pet);
			BeanUtils.copyProperties(pet, petToUpdate, "id","owner","visits","daycares");                                                                                  
                    try {                    
                        this.petService.savePet(petToUpdate);                    
                    } catch (DuplicatedPetNameException ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
                    }
			return "redirect:/owners/{ownerId}";
		}
	}
        
    private void authorizeUserAction(int ownerId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!this.authorizationService.canUserModifyHisData(auth.getName(), ownerId)) {
			throw new AccessDeniedException("User cannot modify data.");
		}
	}
    
    private void authorizeUserActionOnPet(int ownerId, Pet pet) {
    	if (ownerId != pet.getOwner().getId()) throw new AccessDeniedException("User cannot modify data.");
    }
}
