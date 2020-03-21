package org.springframework.samples.petclinic.web;

import java.util.Map;
import javax.validation.Valid;
import java.util.Optional;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.service.DaycareService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/daycares")
public class DaycareController {
	
	
	@Autowired
	private DaycareService daycareService;
  
  private static final String VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM = "daycare/createOrUpdateDaycareForm";

	@GetMapping
	public String listDaycare(@PathVariable int petId, ModelMap modelMap) {
		String vista= "daycares/daycaresList";
		Iterable<Daycare> daycares= daycareService.findDaycaresByPetId(petId);
		modelMap.addAttribute("daycares", daycares);
		return vista;
	}
	
	@GetMapping(path="/delete/{daycareId}")
	public String deleteDaycare(@PathParam("daycareId") int daycareId, ModelMap modelMap) {
		String view="daycares/daycareList";
		Optional<Daycare> daycare=daycareService.findDaycareById(daycareId);
		if(daycare.isPresent()) {
			daycareService.delete(daycare.get());
			modelMap.addAttribute("message","Daycare delete succesfully");
		}else {
			modelMap.addAttribute("message","Daycare not found");

		}
		return view;
	}
  
  @GetMapping(value = "/new")
	public String initTrainingCreationForm(Map<String, Object> model) {
		Daycare daycare = new Daycare();
		model.put("daycare", daycare);
		return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Daycare daycare, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.daycareService.saveDaycare(daycare);
			
			return "redirect:/daycare/" + daycare.getId();
		}
	}
	
	@GetMapping(value = "/{daycareId}/edit")
	public String initUpdateDaycareForm(@PathVariable("daycareId") int daycareId, Model model) {
		Daycare daycare = this.daycareService.findDaycareById(daycareId);
		model.addAttribute(daycare);
		return VIEWS_DAYCARE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{daycareId}/edit")
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
	
	@GetMapping("/{daycareId}")
	public ModelAndView showDaycare(@PathVariable("daycareId") int daycareId) {
		ModelAndView mav = new ModelAndView("trainers/trainerDetails");
		mav.addObject(this.daycareService.findDaycareById(daycareId));
		return mav;
	}
}
