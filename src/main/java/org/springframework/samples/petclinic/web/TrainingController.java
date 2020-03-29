package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoPista;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.repository.TrainerRepository;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.samples.petclinic.util.TrainingDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainingController {
	
	private static final String VIEWS_TRAINING_CREATE_OR_UPDATE_FORM = "trainings/createOrUpdateTrainingForm";
	
	private final PetService petService;
	private final TrainingService trainingService;
	private final TrainerService trainerService;
	
	@Autowired
	public TrainingController(TrainingService trainingService, PetService petService, TrainerService trainerService) {
		this.trainingService = trainingService;
		this.petService = petService;
		this.trainerService = trainerService;
	}
	
	//HE TOCADO ESTO
	@ModelAttribute("trainers")
	public Collection<String> populateTrainers() {
		List<Trainer> trainers = new ArrayList<Trainer>(this.trainerService.findTrainers());
		List<String> lastNames = trainers.stream().map(x->x.getLastName()).collect(Collectors.toList());
		return lastNames;
	}
	
	//HE TOCADO ESTO
	@ModelAttribute("pets")
	public Collection<String> populatePets() {
		String owner = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Pet> pets = this.petService.findPetsByOwner(owner);
		List<String> names = pets.stream().map(x -> x.getName()).collect(Collectors.toList());
		return names;
	}
	
	@ModelAttribute("tipoPistas")
	public Collection<TipoPista> populateTipoPistas() {
		TipoPista[] tipos = TipoPista.class.getEnumConstants();
		return Arrays.asList(tipos);
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
			
			Pet pet;
			Trainer trainer;
			
			try{
				// Integer.parseInt(trainingDTO.getPetId())
				String owner = SecurityContextHolder.getContext().getAuthentication().getName();
				pet = this.petService.findPetsByName(trainingDTO.getPetName(), owner);
			}catch(DataAccessException e){
				result.rejectValue("pet", "Not existance", "Pet does not exist");
                return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			
			try{
				//trainer = this.trainerService.findTrainerByLastName(trainingDTO.getTrainer());
			}catch(DataAccessException e){
				result.rejectValue("trainer", "Not existance", "Trainer does not exist");
                return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}

			Training training = new Training();
			training.setPet(pet);
			training.setPista(trainingDTO.getPista());
			training.setTipoPista(trainingDTO.getTipoPista());
			training.setDescription(trainingDTO.getDescription());
			training.setDate(trainingDTO.getDate());
			//pet.addTraining(training);
			this.trainingService.saveTraining(training);
			
			return "redirect:/trainings/" + training.getId();
		}
	}
	
	@GetMapping("/trainings/{trainingId}")
	public ModelAndView showTraining(@PathVariable("trainingId") int trainingId) {
		ModelAndView mav = new ModelAndView("trainings/trainingDetails");
		mav.addObject(this.trainingService.findTrainingById(trainingId));
		return mav;
	}
	
	@GetMapping(value = "/trainings")
	public String showTrainingsList(Map<String, Object> model) {
		Collection<Training> results = this.trainingService.findTrainings();
		model.put("trainings", results);
		return "trainings/trainingsList";
	}
	
	@GetMapping(value = "/trainings/{trainingId}/delete")
	public String processDeleteTrainingForm(@PathVariable("trainingId") int trainingId) {
		this.trainingService.delete(trainingId);
		return "redirect:/trainings";
	}

	@GetMapping(value = "/trainings/{trainingId}/edit")
	public String initUpdateTrainingForm(@PathVariable("trainingId") int trainingId, Model model) {
		
		Training training = this.trainingService.findTrainingById(trainingId);
		
		TrainingDTO trainingDTO = new TrainingDTO();
		trainingDTO.setDate(training.getDate());
		trainingDTO.setDescription(training.getDescription());
		trainingDTO.setPista(training.getPista());
		trainingDTO.setTipoPista(training.getTipoPista());
		trainingDTO.setPetName(training.getPet().getName());
		trainingDTO.setTrainer(training.getTrainer().getLastName());
		
		model.addAttribute("boton", false);
		model.addAttribute(trainingDTO);
		//meter que el atributo "new" sea false, en la variable training
		return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/trainings/{trainingId}/edit")
	public String processUpdateTrainingForm(@Valid TrainingDTO trainingDTO, BindingResult result,
			@PathVariable("trainingId") int trainingId) {
		if (result.hasErrors()) {
			return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
		}
		else {
			Pet pet;
			//Trainer trainer;
			
			try{
				String owner = SecurityContextHolder.getContext().getAuthentication().getName();
				pet = this.petService.findPetsByName(trainingDTO.getPetName(), owner);
				//pet = this.petService.findPetById(Integer.parseInt(trainingDTO.getPetId()));
			}catch(DataAccessException e){
				result.rejectValue("pet", "Not existance", "Pet does not exist");
                return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
			}
			

			Training training = this.trainingService.findTrainingById(trainingId);
			training.setPet(pet);
			training.setPista(trainingDTO.getPista());
			training.setTipoPista(trainingDTO.getTipoPista());
			training.setDescription(trainingDTO.getDescription());
			training.setDate(trainingDTO.getDate());
			this.trainingService.saveTraining(training);
//			training.setId(ownerId);
//			this.trainingService.saveTraining(training);
			return "redirect:/trainings/{trainingId}";
		}
	}
}
