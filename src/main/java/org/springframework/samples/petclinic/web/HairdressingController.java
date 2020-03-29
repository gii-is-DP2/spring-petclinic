package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HairdressingController {
	
	@Autowired
	private HairdressingService hairdressingService;
	
	@Autowired
	private PetService petService;
	
	@ModelAttribute("tiposCuidados")
	public Collection<TipoCuidado> populateTiposCuidado() {
		List<TipoCuidado> res = new ArrayList<TipoCuidado>();
		res.add(TipoCuidado.ESTETICA);
		res.add(TipoCuidado.PELUQUERIA);
		return res;
	}
	
	@ModelAttribute("horasDisponibles")
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
	
//	@ModelAttribute("mascotas")
//	public List<Integer> populateMascotas() {
//		List<Integer> res = new ArrayList<Integer>();
//		res.add(petService.findPetById(1).getId());
//		res.add(petService.findPetById(2).getId());
//		return res;
//	}
//		
//	@GetMapping("/list")
//	public String listHairdressingAppointments(ModelMap modelMap) {
//		String vista = "hairdressing/listHairdressings";
//		Iterable<Hairdressing> hairdressings = hairdressingService.findAll();
//		modelMap.addAttribute("hairdressings", hairdressings);
//		return vista;
//	}
//	
//	@GetMapping(path="/new")
//	public String crearAppointment(ModelMap modelMap) {
//		String view = "hairdressing/editHairdressing";
//		modelMap.addAttribute("hairdressing", new Hairdressing());
//		return view;
//	}
//	
//	@PostMapping(path = "/save")
//	public String salvarHairdressing(@Valid Hairdressing hairdressing, BindingResult result, ModelMap modelMap) {
//		String view = "/hairdressing/listHairdressings";
//		System.out.println("\n\n\n\n" + hairdressing + "\n\n\n\n");
//		if(result.hasErrors()) {
//			return "hairdressing/editHairdressing";
//		}else {
//			
//			hairdressingService.save(hairdressing);
////			modelMap.addAttribute("message", "Appointment succesfully saved!");
////			view = listHairdressingAppointments(modelMap);
//			return view;
//		}
//		
//	}
	
//	@GetMapping(path="/delete/{hairdressingId}")
//	public String borrarHairdressing(@PathVariable("hairdressingId") int hairdressingId, ModelMap modelMap) {
//		String view = "hairdressing/listHairdressings";
//		Optional<Hairdressing> hairdressing = hairdressingService.findHairdressingById(hairdressingId);
//		if(hairdressing.isPresent()) {
//			hairdressingService.delete(hairdressing.get());
//			modelMap.addAttribute("message", "Appointment succesfully deleted!");
//		}else {
//			modelMap.addAttribute("message", "Event not found!");
//		}
//		
//		return this.listHairdressingAppointments(modelMap);
//	}
	
	//@ModelAttribute("hairdressing")
	//public Hairdressing loadPetWithHairdressing(@PathVariable("petId") int petId) {
		//Pet pet = this.petService.findPetById(petId);
		//Hairdressing hairdressing = new Hairdressing();
		//pet.addHairdressing(hairdressing);
		//return hairdressing;
	//}
	
	@InitBinder("hairdressing")
	public void initHairdressingBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new HairdressingValidator());
	}
	
	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/hairdressing/new")
	public String initNewHairdressingForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		Pet pet = this.petService.findPetById(petId);
		Hairdressing hairdressing = new Hairdressing();
		pet.addHairdressing(hairdressing);
		model.put("hairdressing", hairdressing);
		return "pets/createOrUpdateHairdressingForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hairdressing/new")
	public String processNewHairdressingForm(@PathVariable("petId") int petId, @PathVariable("ownerId") int ownerId, @Valid Hairdressing hairdressing, BindingResult result) {
		Pet pet = this.petService.findPetById(petId);
		hairdressing.setPet(pet);
		if (result.hasErrors()) {
			return "pets/createOrUpdateHairdressingForm";
		}
		else {
			if (hairdressingService.countHairdressingsByDateAndTime(hairdressing.getDate(), hairdressing.getTime()) != 0){
				result.rejectValue("time", "", "This time isn't available, please select another");
				return "pets/createOrUpdateHairdressingForm";
				
			}else {
				this.petService.saveHairdressing(hairdressing);
				System.out.println("\n\n\n\n Estos son los hairdressings que hay: \n\n\n\n"+this.petService.findPetById(petId).getHairdressings()+"\n\n\n\n");
				return "redirect:/owners/"+ ownerId;
			}
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/hairdressings")
	public String showHairdressings(@PathVariable int petId, Map<String, Object> model) {
		model.put("hairdressings", this.petService.findPetById(petId).getHairdressings());
		
		return "hairdressingList";
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/hairdressing/{hairdressingId}/delete")
	public String deleteHairdressing(@PathVariable("ownerId") int ownerId, @PathVariable int hairdressingId) {
		hairdressingService.delete(hairdressingId);
		return "redirect:/owners/"+ ownerId;
	}
	
}
