package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.GroundType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.service.exceptions.MappingException;
import org.springframework.samples.petclinic.util.TrainingDTO;
import org.springframework.samples.petclinic.web.annotations.IsAdmin;
import org.springframework.samples.petclinic.web.annotations.IsAuthenticated;
import org.springframework.samples.petclinic.web.annotations.IsOwner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@IsAuthenticated
@Controller
public class TrainingController {
	
	private static final String VIEWS_TRAINING_CREATE_OR_UPDATE_FORM = "trainings/createOrUpdateTrainingForm";
	private static final String UNAUTHORIZED = "errors/accessDenied";
	
	private final PetService petService;
	private final TrainingService trainingService;
	private final TrainerService trainerService;
	private final AuthorizationService authorizationService;
	
	@Autowired
	public TrainingController(TrainingService trainingService, PetService petService, TrainerService trainerService, AuthorizationService authorizationService) {
		this.trainingService = trainingService;
		this.petService = petService;
		this.trainerService = trainerService;
		this.authorizationService = authorizationService;
	}
	
	@ModelAttribute("trainers")
	public Collection<Trainer> populateTrainers() {
		List<Trainer> trainers = new ArrayList<Trainer>(this.trainerService.findTrainers());
		return trainers;
	}
	
	@ModelAttribute("pets")
	public Collection<String> populatePets() {
		String owner = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Pet> pets = this.petService.findPetsByOwner(owner);
		List<String> names = pets.stream().map(x -> x.getName()).collect(Collectors.toList());
		return names;
	}
	
	@ModelAttribute("groundTypes")
	public Collection<GroundType> populateGroundTypes() {
		GroundType[] types = GroundType.class.getEnumConstants();
		return Arrays.asList(types);
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/trainings/new")
	public String initTrainingCreationForm(Map<String, Object> model) {
		TrainingDTO trainingDTO = new TrainingDTO();
		model.put("trainingDTO", trainingDTO);
		model.put("boton", true);
		return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/trainings/new")
	public String processCreationForm(@Valid TrainingDTO trainingDTO, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
		}
		else {
			Training training;
			
			try {
				training = this.convertToEntity(trainingDTO);
			} catch (MappingException ex) {
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
	            return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			try {
				this.trainingService.saveTraining(training);
			} catch (BusinessException ex) {
				result.rejectValue(ex.getField(), ex.getCode(), ex.getMessage());
				return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			
			return "redirect:/trainings/" + training.getId();
		}
	}

	@GetMapping(value = "/trainings/{trainingId}/edit")
	public String initUpdateTrainingForm(@PathVariable("trainingId") int trainingId, Model model) {
		
		Training training = this.trainingService.findTrainingById(trainingId);
		this.authorizeUserAction(training.getPet().getId());
		
		TrainingDTO trainingDTO = this.convertToDto(training);
		
		model.addAttribute("boton", false);
		model.addAttribute(trainingDTO);
		
		return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/trainings/{trainingId}/edit")
	public String processUpdateTrainingForm(@Valid TrainingDTO trainingDTO, BindingResult result, @PathVariable("trainingId") int trainingId) {
		if (result.hasErrors()) {
			return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
		}
		else {
			Training oldTraining = this.trainingService.findTrainingById(trainingId);

			Training training;
			
			try {
				training = this.convertToEntity(trainingDTO);
				training.setId(trainingId);
			} catch (MappingException ex) {
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
	            return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			
			this.authorizeUserAction(training.getPet().getId());
						
			try {
				this.trainingService.saveTraining(training);
			} catch (BusinessException ex) {
				result.rejectValue(ex.getField(), ex.getCode(), ex.getMessage());
				return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			
			return "redirect:/trainings/{trainingId}";
		}
	}

	@GetMapping("/trainings/{trainingId}")
	public ModelAndView showTraining(@PathVariable("trainingId") int trainingId) {
		ModelAndView mav = new ModelAndView("trainings/trainingDetails");
		mav.addObject(this.trainingService.findTrainingById(trainingId));
		return mav;
	}
	
	@IsAdmin
	@GetMapping(value = "/trainings")
	public String showTrainingsList(Map<String, Object> model) {
		Collection<Training> results = this.trainingService.findTrainings();
		model.put("trainings", results);
		
		return "trainings/trainingsList";
	}
	
	@IsOwner
	@GetMapping(value = "/trainings/owner")
	public String showOwnerTrainingsList(Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<Training> results = this.trainingService.findTrainingsByUser(auth.getName());
		model.put("trainings", results);
		
		return "trainings/trainingsList";
	}
	
	@GetMapping(value = "/trainings/{trainingId}/delete")
	public String processDeleteTrainingForm(@PathVariable("trainingId") int trainingId) {
		Training training = this.trainingService.findTrainingById(trainingId);
		this.authorizeUserAction(training.getPet().getId());
		this.trainingService.delete(trainingId);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/trainings";
		}
		return "redirect:/trainings/owner";
	}
	
	private Training convertToEntity(TrainingDTO dto) throws MappingException {
		Training training = new Training();
		
		String owner = SecurityContextHolder.getContext().getAuthentication().getName();
		Pet pet = this.petService.findPetsByName(dto.getPetName(), owner);
		if (pet == null) {
			throw new MappingException("petName", "Not existance", "Pet does not exist");
		}
		training.setPet(pet);
		
		try {
			Trainer trainer = this.trainerService.findTrainerById(dto.getTrainerId());
			training.setTrainer(trainer);
		} catch(NoSuchElementException e) {
			throw new MappingException("trainerId", "Not existance", "Trainer does not exist");
		}

		training.setGround(dto.getGround());
		training.setGroundType(dto.getGroundType());
		training.setDescription(dto.getDescription());
		training.setDate(dto.getDate());
		
		return training;
	}

	private TrainingDTO convertToDto(Training entity) {
		TrainingDTO dto = new TrainingDTO();
		dto.setDate(entity.getDate());
		dto.setDescription(entity.getDescription());
		dto.setGround(entity.getGround());
		dto.setGroundType(entity.getGroundType());
		dto.setPetName(entity.getPet().getName());
		dto.setTrainerId(entity.getTrainer().getId());
		return dto;
	}
	
	private void authorizeUserAction(int petId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!this.authorizationService.canUserModifyBooking(auth.getName(), petId)) {
			throw new AccessDeniedException("User canot modify data.");
		}
	}
}
