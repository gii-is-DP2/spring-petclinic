package org.springframework.samples.petclinic.web;

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
	
	@GetMapping("/daycares/{daycareId}")
	public ModelAndView showTrainer(@PathVariable("daycareId") int daycareId) {
		ModelAndView mav = new ModelAndView("trainers/trainerDetails");
		mav.addObject(this.daycareService.findDaycareById(daycareId));
		return mav;
	}
}
