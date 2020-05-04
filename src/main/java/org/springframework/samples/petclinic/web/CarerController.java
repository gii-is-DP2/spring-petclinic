package org.springframework.samples.petclinic.web;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Carer;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.CarerService;
import org.springframework.samples.petclinic.service.exceptions.MappingException;
import org.springframework.samples.petclinic.web.annotations.IsAdmin;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.j2objc.annotations.AutoreleasePool;

@IsAdmin
@Controller
public class CarerController {
	
	private static final String VIEWS_CARER_CREATE_OR_UPDATE_FORM = "carers/createOrUpdateCarerForm";

	private final CarerService carerService;
	
	private final AuthorizationService authorizationService;

	
	@Autowired
	public CarerController(CarerService carerService , final AuthorizationService authorizationService) {
		this.carerService = carerService;
		this.authorizationService = authorizationService;
	}
	
	@ModelAttribute("isHairdresser")
	public Collection<String> populateHairdresser() {
		List<String> res = new ArrayList<String>();
		res.add("Yes");
		res.add("No");
		return res;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/carers/new")
	public String initCarerCreationForm(Map<String, Object> model) {
		Carer carer = new Carer();
		model.put("carer", carer);
		return VIEWS_CARER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/carers/new")
	public String processCreationForm(@Valid Carer carer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CARER_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.carerService.saveCarer(carer);
			
			return "redirect:/carers/" + carer.getId();
		}
	}
	
	@GetMapping(value = "/carers/{carerId}/edit")
	public String initUpdateCarerForm(@PathVariable("carerId") int carerId, Model model) {
		this.authorizeUserAction();
		Carer carer = this.carerService.findCarerById(carerId);
		model.addAttribute("carer", carer);
		return VIEWS_CARER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/carers/{carerId}/edit")
	public String processUpdateCarerForm(@Valid Carer carer, BindingResult result,
			@PathVariable("carerId") int carerId) {
		
		carer.setId(carerId);
		
		if (result.hasErrors()) {
			return VIEWS_CARER_CREATE_OR_UPDATE_FORM;
		}
		else {            
            carerService.saveCarer(carer);
            return "redirect:/carers";
        }
	}
	
	@GetMapping("/carers/{carerId}")
	public ModelAndView showCarer(@PathVariable("carerId") int carerId) {
		ModelAndView mav = new ModelAndView("carers/carerDetails");
		mav.addObject(this.carerService.findCarerById(carerId));
		return mav;
	}
	
	@GetMapping(value = "/carers")
	public String initFindForm(Map<String, Object> model) {
		Collection<Carer> results = this.carerService.findCarers();
		model.put("carers", results);
		Carer carer = new Carer();
		model.put("carer", carer);
		return "carers/carersList";
	}
	
	@GetMapping(value = "/carer/find")
	public String showTrainersList(Carer carer, BindingResult result, Map<String, Object> model) {
		Collection<Carer> results;
		
		if (carer == null || carer.getLastName() == null || carer.getLastName().isEmpty()) {
			results = this.carerService.findCarers();
		} else {
			results = this.carerService.findCarersByLastName(carer.getLastName());
		}

		model.put("carers", results);
		return "carers/carersList";
	}
	
	@GetMapping(value= "/carers/{carerId}/delete")
	public String deleteCarer(@PathVariable("carerId") final int carerId, final ModelMap model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			throw new AccessDeniedException("User cannot modify data.");
		} 
		
		Carer carer = this.carerService.findCarerById(carerId);
		
		this.carerService.delete(carerId);
	
		
		
		return "redirect:/carers";
		
	}
	
	
	private void authorizeUserAction() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!this.authorizationService.canUserModifyEmployee(auth.getName() )) {
			throw new AccessDeniedException("User cannot modify data.");
		}
	}
	
	
}
