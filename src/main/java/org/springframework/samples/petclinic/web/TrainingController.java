package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Training;
import org.springframework.samples.petclinic.service.TrainingService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainingController {
	
	private static final String VIEWS_TRAINING_CREATE_OR_UPDATE_FORM = "training/createOrUpdateTrainingForm";
	
	private final TrainingService trainingService;
	
	@Autowired
	public TrainingController(TrainingService trainingService) {
		this.trainingService = trainingService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/training/new")
	public String initTrainingCreationForm(Map<String, Object> model) {
		Training training = new Training();
		model.put("training", training);
		return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/training/new")
	public String processCreationForm(@Valid Training training, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_TRAINING_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.trainingService.saveTraining(training);
			
			return "redirect:/training/" + training.getId();
		}
	}
	
	@GetMapping("/training/{trainingId}")
	public ModelAndView showTraining(@PathVariable("trainingId") int trainingId) {
		ModelAndView mav = new ModelAndView("training/trainingDetails");
		mav.addObject(this.trainingService.findTrainingById(trainingId));
		return mav;
	}
	
	@GetMapping(value = "/trainings")
	public String showTrainingsList(Map<String, Object> model) {
		Collection<Training> results = this.trainingService.findTrainings();
		model.put("training", results);
		return "training/trainingsList";
	}
	
}
