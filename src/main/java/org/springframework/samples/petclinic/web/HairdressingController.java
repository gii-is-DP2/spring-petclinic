package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.service.AuthorizationService;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.BusinessException;
import org.springframework.samples.petclinic.service.exceptions.MappingException;
import org.springframework.samples.petclinic.util.HairdressingDTO;
import org.springframework.samples.petclinic.web.annotations.IsAdmin;
import org.springframework.samples.petclinic.web.annotations.IsOwner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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

@Controller

public class HairdressingController {
	
	private static final String VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM = "hairdressings/createOrUpdateHairdressingForm";
	
	@Autowired
	private HairdressingService hairdressingService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private PetService petService;
	
	@ModelAttribute("tiposCuidados")
	public Collection<TipoCuidado> populateTiposCuidado() {
		List<TipoCuidado> res = new ArrayList<TipoCuidado>();
		res.add(TipoCuidado.ESTETICA);
		res.add(TipoCuidado.PELUQUERIA);
		return res;
	}
	
	@ModelAttribute("availableTimes")
	public Collection<String> populateHorasDisponibles() {
		List<String> res = new ArrayList<String>();
		res.add("6:00");
		res.add("6:30");
		res.add("7:00");
		res.add("7:30");
		res.add("8:00");
		res.add("8:30");
		res.add("9:00");
		res.add("9:30");
		return res;
	}
	
	@ModelAttribute("pets")
	public Collection<String> populatePets() {
		String owner = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Pet> pets = this.petService.findPetsByOwner(owner);
		List<String> names = pets.stream().map(x -> x.getName()).collect(Collectors.toList());
		return names;
	}
	
	@InitBinder("hairdressing")
	public void initHairdressingBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new HairdressingValidator());
	}
	
	@GetMapping(value = "/hairdressings/new")
	public String initHairdressingCreationForm(Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/errors/accessDenied";
		}
		HairdressingDTO hairdressingDTO = new HairdressingDTO();
		model.put("hairdressingDTO", hairdressingDTO);
		model.put("boton", true);
		return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/hairdressings/new")
	public String processCreationForm(@Valid HairdressingDTO hairdressingDTO, BindingResult result) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/errors/accessDenied";
		}
		if (result.hasErrors()) {
			
			return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
		}
		else {
			
			Hairdressing hairdressing;
			
			
			try {
				hairdressing = this.convertToEntity(hairdressingDTO);
				
			} catch (MappingException ex) {
				
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
				
	            return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			if(!(hairdressing.getDate().isAfter(LocalDate.now()))) {
				result.rejectValue("date", "", "Please, select a future date");
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}if(hairdressingService.countHairdressingsByDateAndTime(hairdressing.getDate(), hairdressing.getTime()) > 0) {
				result.rejectValue("time", "", "This time has already been taken. Please, select another time");
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			try {
				this.hairdressingService.save(hairdressing);
			
			} catch (BusinessException ex) {
				
				result.rejectValue(ex.getField(), ex.getCode(), ex.getMessage());
				
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			
			return "redirect:/hairdressings/owner";
		}
	}

	@GetMapping(value = "/hairdressings/{hairdressingId}/edit")
	public String initUpdateHairdressingForm(@PathVariable("hairdressingId") int hairdressingId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/errors/accessDenied";
		}
		Hairdressing hairdressing = this.hairdressingService.findHairdressingById(hairdressingId);
		this.authorizeUserAction(hairdressing.getPet().getId());
		
		HairdressingDTO hairdressingDTO = this.convertToDto(hairdressing);
		
		model.addAttribute("boton", false);
		model.addAttribute(hairdressingDTO);
		return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/hairdressings/{hairdressingId}/edit")
	public String processUpdateHairdressingForm(@Valid HairdressingDTO hairdressingDTO, BindingResult result, @PathVariable("hairdressingId") int hairdressingId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			return "redirect:/errors/accessDenied";
		}
		if (result.hasErrors()) {
			return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
		}
		else {
			Hairdressing hairdressing;
			try {
				hairdressing = this.convertToEntity(hairdressingDTO);
				hairdressing.setId(hairdressingId);
			} catch (MappingException ex) {
				result.rejectValue(ex.getEntity(), ex.getError(), ex.getMessage());
	            return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			if(!(hairdressing.getDate().isAfter(LocalDate.now()))) {
				result.rejectValue("date", "", "Please, select a future date");
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}if(hairdressingService.countHairdressingsByDateAndTime(hairdressing.getDate(), hairdressing.getTime()) > 0) {
				result.rejectValue("time", "", "This time has already been taken. Please, select another time");
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			try {
				this.hairdressingService.save(hairdressing);
			} catch (BusinessException ex) {
				result.rejectValue(ex.getField(), ex.getCode(), ex.getMessage());
				return VIEWS_HAIRDRESSING_CREATE_OR_UPDATE_FORM;
			}
			
			return "redirect:/hairdressings/{hairdressingId}";
		}
	}

	@GetMapping("/hairdressings/{hairdressingId}")
	public ModelAndView showHairdressing(@PathVariable("hairdressingId") int hairdressingId, Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {
			model.put("owner", false);
		}else {
			model.put("owner", true);
		}
		System.out.println("\n\n··········"+model.get("owner")+"··········\n\n");
		Hairdressing hairdressing = hairdressingService.findHairdressingById(hairdressingId);
		this.authorizeUserAction(hairdressing.getPet().getId());
		ModelAndView mav = new ModelAndView("hairdressings/hairdressingDetails");
		mav.addObject(this.hairdressingService.findHairdressingById(hairdressingId));
		return mav;
	}
	
	@IsAdmin
	@GetMapping(value = "/hairdressings")
	public String showHairdressingList(Map<String, Object> model) {
		Collection<Hairdressing> results = (Collection) this.hairdressingService.findAll();
		model.put("hairdressings", results);
		model.put("owner", false);
		
		return "hairdressings/hairdressingsList";
	}
	
	@IsOwner
	@GetMapping(value = "/hairdressings/owner")
	public String showOwnerHairdressingsList(Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.put("owner", true);
		Collection<Hairdressing> results = this.hairdressingService.findHairdressingsByUser(auth.getName());
		model.put("hairdressings", results);

		return "hairdressings/hairdressingsList";
	}
	
	@GetMapping(value = "/hairdressings/{hairdressingId}/delete")
	public String processDeleteHairdressingForm(@PathVariable("hairdressingId") int hairdressingId) {
		Hairdressing h = hairdressingService.findHairdressingById(hairdressingId);
		this.authorizeUserAction(h.getPet().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().map(x -> x.getAuthority()).anyMatch(x -> x.equals("admin"))) {

			if (h.getDate().isEqual(LocalDate.now()) || h.getDate().isEqual(LocalDate.now().plusDays(1))) {
				return "redirect:/hairdressings";

			} else {
				this.hairdressingService.delete(hairdressingId);

				return "redirect:/hairdressings";
			}
		} else {

			if (h.getDate().isEqual(LocalDate.now()) || h.getDate().isEqual(LocalDate.now().plusDays(1))) {
				return "redirect:/hairdressings/owner";

			} else {
				this.hairdressingService.delete(hairdressingId);

				return "redirect:/hairdressings/owner";
			}
		}
	}
	
	private Hairdressing convertToEntity(HairdressingDTO dto) throws MappingException {
		Hairdressing hairdressing = new Hairdressing();
		
		try {
			String owner = SecurityContextHolder.getContext().getAuthentication().getName();
			Pet pet = this.petService.findPetsByName(dto.getPetName(), owner);
			hairdressing.setPet(pet);
		} catch(DataAccessException e) {
			throw new MappingException("pet", "Not existance", "Pet does not exist");
		}
		
		

		hairdressing.setCuidado(dto.getCuidado());
		hairdressing.setDate(dto.getDate());
		hairdressing.setDescription(dto.getDescription());
		hairdressing.setTime(dto.getTime());
		
		return hairdressing;
	}

	private HairdressingDTO convertToDto(Hairdressing entity) {
		HairdressingDTO dto = new HairdressingDTO();
		dto.setDate(entity.getDate());
		dto.setDescription(entity.getDescription());
		dto.setPetName(entity.getPet().getName());

		return dto;
	}
	
	private void authorizeUserAction(int petId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!this.authorizationService.canUserModifyBooking(auth.getName(), petId)) {
			throw new AccessDeniedException("User cannot modify data.");
		}
	}
	
}