package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.web.annotations.IsAdmin;
import org.springframework.samples.petclinic.web.annotations.IsOwner;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@IsAdmin
@Controller
public class TrainerController {
	
	private static final String VIEWS_TRAINER_CREATE_OR_UPDATE_FORM = "trainers/createOrUpdateTrainerForm";
	
	private final TrainerService trainerService;
	
	@Autowired
	public TrainerController(TrainerService trainerService) {
		this.trainerService = trainerService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@GetMapping(value = "/trainers/new")
	public String initTrainerCreationForm(Map<String, Object> model) {
		Trainer trainer = new Trainer();
		model.put("trainer", trainer);
		return VIEWS_TRAINER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/trainers/new")
	public String processCreationForm(@Valid Trainer trainer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_TRAINER_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.trainerService.saveTrainer(trainer);
			
			return "redirect:/trainers/" + trainer.getId();
		}
	}
	
	@GetMapping("/trainers/{trainerId}")
	public ModelAndView showTrainer(@PathVariable("trainerId") int trainerId) {
		ModelAndView mav = new ModelAndView("trainers/trainerDetails");
		mav.addObject(this.trainerService.findTrainerById(trainerId));
		return mav;
	}
	
	@GetMapping(value = "/trainers")
	public String initFindForm(Map<String, Object> model) {
		model.put("trainer", new Trainer());
		Collection<Trainer> results = this.trainerService.findTrainers();
		model.put("trainers", results);
		return "trainers/trainersList";
	}
	
	@GetMapping(value = "/trainers/find")
	public String showTrainersList(Trainer trainer, BindingResult result, Map<String, Object> model) {
		Collection<Trainer> results;
		
		if (trainer.getLastName().isEmpty()) {
			results = this.trainerService.findTrainers();
		} else {
			results = this.trainerService.findTrainersByLastName(trainer.getLastName());
		}

		model.put("trainers", results);
		return "trainers/trainersList";
	}
	
}
