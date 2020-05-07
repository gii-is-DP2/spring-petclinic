package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Daycare;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.DaycareService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.service.exceptions.MappingException;
import org.springframework.samples.petclinic.util.DaycareDTO;
import org.springframework.samples.petclinic.web.annotations.IsAdmin;
import org.springframework.samples.petclinic.web.annotations.IsOwner;
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

@Controller
public class DaycareController {
	
	private PetService petService;
	
	private final DaycareService daycareService;
	
	private final AuthorizationService authorizationService;
	
	@Autowired
	public DaycareController(final PetService petService,final DaycareService daycareService, final AuthorizationService authorizationService) {
		this.petService = petService;
		this.daycareService = daycareService;
		this.authorizationService= authorizationService;
	}
	
	@ModelAttribute("pets")
	public Collection<String> populatePets() {
		String owner = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Pet> pets = this.petService.findPetsByOwner(owner);
		List<String> names = pets.stream().map(x -> x.getName()).collect(Collectors.toList());
		return names;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("aforo");
	}
	
	@GetMapping(value= "/daycares/{daycareId}/delete")
	public String deleteDaycare(@PathVariable("daycareId") final int daycareId, final Pet pet, final ModelMap model) {
		Daycare daycare= this.daycareService.findDaycareById(daycareId);
		this.authorizeUserAction(daycare.getPet().getId());
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		Date minimumDeadline = calendar.getTime();
		if(daycare.getDate().isBefore(minimumDeadline.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())){
			return "redirect:/daycares/owner";
		} else {
			this.daycareService.delete(daycareId);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/daycares";
		}
		
		return "redirect:/daycares/owner";
	}
  
	@GetMapping(value = "/daycares/new")
	public String initNewDaycareForm(Map<String, Object> model) {
		DaycareDTO daycareDTO = new DaycareDTO();
		model.put("daycareDTO", daycareDTO);
		model.put("nuevo", true);
		return "daycares/createOrUpdateDaycareForm";
	}
	
	@PostMapping(value = "/daycares/new")
	public String processNewDaycareForm(@Valid DaycareDTO daycareDTO, BindingResult result) {
		
//		Daycare daycare;

		if (result.hasErrors()) {
			return "daycares/createOrUpdateDaycareForm";
		}
		else {
			Daycare daycare;
			try {
				daycare = this.convertToEntity(daycareDTO);
			} catch (MappingException ex) {
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
	            return "daycares/createOrUpdateDaycareForm";
			}
			
			if(this.daycareService.oneDaycareById(daycare.getDate(), daycare.getPet().getId())==1){
                result.rejectValue("date", "", "This daycare already exists");
    			return "daycares/createOrUpdateDaycareForm";
			} else if(this.daycareService.countDaycareByDate(daycare.getDate())==daycare.getCapacity()) {
				result.rejectValue("date", "", "Complete capacity. Please, select another date");
    			return "daycares/createOrUpdateDaycareForm";
			
			} else if(daycare.getDate().isBefore(LocalDate.now())) {
				result.rejectValue("date", "", "Please, select a future date");
    			return "daycares/createOrUpdateDaycareForm";
			}
			
			this.daycareService.saveDaycare(daycare);	
			return "redirect:/daycares/" + daycare.getId();

		}
		
	}
	
	@GetMapping(value = "/daycares/{daycareId}/edit")
	public String initUpdateDaycareForm(@PathVariable("daycareId") int daycareId, Model model) {
		Daycare daycare = this.daycareService.findDaycareById(daycareId);
		this.authorizeUserAction(daycare.getPet().getId());
		DaycareDTO daycareDTO = this.convertToDto(daycare);
		model.addAttribute("nuevo", false);
		model.addAttribute(daycareDTO);
		return "daycares/createOrUpdateDaycareForm";
	}

	@PostMapping(value = "/daycares/{daycareId}/edit")
	public String processUpdateDaycareForm(@Valid DaycareDTO daycareDTO, BindingResult result,
			@PathVariable("daycareId") int daycareId) {
		if (result.hasErrors()) {
			return "daycares/createOrUpdateDaycareForm";
		}
		else {
//            daycareService.delete(daycareId);
            
			Daycare daycare;

            try {
				daycare = this.convertToEntity(daycareDTO);
				daycare.setId(daycareId);
			} catch (MappingException ex) {
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
	            return "daycares/createOrUpdateDaycareForm";
			}
            
			this.authorizeUserAction(daycare.getPet().getId());

            if(this.daycareService.oneDaycareById(daycare.getDate(), daycare.getPet().getId())==1){
                result.rejectValue("date", "", "This daycare already exists");
                return "daycares/createOrUpdateDaycareForm";
            } else if(this.daycareService.countDaycareByDate(daycare.getDate())==daycare.getCapacity()) {
                result.rejectValue("date", "", "Complete capacity. Please, select another date");
                return "daycares/createOrUpdateDaycareForm";

            } else if(daycare.getDate().isBefore(LocalDate.now())) {
                result.rejectValue("date", "", "Please, select a future date");
                return "daycares/createOrUpdateDaycareForm";
            }
            
            
            daycare.setId(daycareId);
            daycareService.saveDaycare(daycare);
            return "redirect:/daycares/owner";
        }
	}
	
	@IsAdmin
	@GetMapping(value = "/daycares")
	public String showDaycares(Map<String, Object> model) {		
		model.put("owner", false);
		Collection<Daycare> results = this.daycareService.findDaycares();
		model.put("daycares", results);
		return "daycares/daycaresList";
	}
	
	@GetMapping(value = "/daycares/{daycareId}")
	public ModelAndView showDaycare(@PathVariable("daycareId") int daycareId, Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			model.put("owner", true);
		}else {
			model.put("owner", false);
		}
		Daycare daycare= this.daycareService.findDaycareById(daycareId);
		this.authorizeUserAction(daycare.getPet().getId());
		ModelAndView mav = new ModelAndView("daycares/daycareDetails");
		mav.addObject(this.daycareService.findDaycareById(daycareId));
		return mav;
	}
	
	private Daycare convertToEntity(DaycareDTO dto) throws MappingException {
		Daycare daycare = new Daycare();
		
		try {
			String owner = SecurityContextHolder.getContext().getAuthentication().getName();
			Pet pet = this.petService.findPetsByName(dto.getPetName(), owner);
			daycare.setPet(pet);
		} catch(DataAccessException e) {
			throw new MappingException("pet", "Not existance", "Pet does not exist");
		}

		daycare.setCapacity(dto.getCapacity());
		daycare.setDescription(dto.getDescription());
		daycare.setDate(dto.getDate());
		
		return daycare;
	}
	
	@IsOwner
	@GetMapping(value = "/daycares/owner")
	public String showOwnerDaycaresList(Map<String, Object> model) {
		model.put("owner", true);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<Daycare> results = this.daycareService.findDaycaresByUser(auth.getName());
		model.put("daycares", results);
		
		return "daycares/daycaresList";
	}

	private DaycareDTO convertToDto(Daycare entity) {
		DaycareDTO dto = new DaycareDTO();
		dto.setDate(entity.getDate());
		dto.setDescription(entity.getDescription());
		dto.setCapacity(entity.getCapacity());
		dto.setPetName(entity.getPet().getName());
		return dto;
	}
	
	private void authorizeUserAction(int petId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!this.authorizationService.canUserModifyBooking(auth.getName(), petId)) {
			throw new AccessDeniedException("User canot modify data.");
		}
	}
	
}
