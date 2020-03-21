package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.service.DaycareService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DaycareController {

	private static final String VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM = "daycare/createOrUpdateDaycareForm";
	
	private final DaycareService daycareService;
	
	@Autowired
	public DaycareController(DaycareService daycareService) {
		this.daycareService = daycareService;
	}
	
	@GetMapping(value = "/daycare/new")
	public String initTrainingCreationForm(Map<String, Object> model) {
		Daycare daycare = new Daycare();
		model.put("daycare", daycare);
		return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/daycare/new")
	public String processCreationForm(@Valid Daycare daycare, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.daycareService.saveDaycare(daycare);
			
			return "redirect:/daycare/" + daycare.getId();
		}
	}
	
	@GetMapping(value = "/daycare/{daycareId}/edit")
	public String initUpdateDaycareForm(@PathVariable("daycareId") int daycareId, Model model) {
		Daycare daycare = this.daycareService.findDaycareById(daycareId);
		model.addAttribute(daycare);
		return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/daycare/{daycareId}/edit")
	public String processUpdateDaycareForm(@Valid Daycare daycare, BindingResult result,
			@PathVariable("daycareId") int daycareId) {
		if (result.hasErrors()) {
			return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
		}
		else {
			daycare.setId(daycareId);
			this.daycareService.saveDaycare(daycare);
			return "redirect:/daycare/{daycareId}";
		}
	}
	
	@GetMapping("/daycare/{daycareId}")
	public ModelAndView showTraining(@PathVariable("daycareId") int daycareId) {
		ModelAndView mav = new ModelAndView("daycare/daycareDetails");
		mav.addObject(this.daycareService.findDaycareById(daycareId));
		return mav;
	}
	
	
	
}
