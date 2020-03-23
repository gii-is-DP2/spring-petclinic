package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hairdressing")
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
	
	@ModelAttribute("mascotas")
	public List<Integer> populateMascotas() {
		List<Integer> res = new ArrayList<Integer>();
		res.add(petService.findPetById(1).getId());
		res.add(petService.findPetById(2).getId());
		return res;
	}
		
	@GetMapping("/list")
	public String listHairdressingAppointments(ModelMap modelMap) {
		String vista = "hairdressing/listHairdressings";
		Iterable<Hairdressing> hairdressings = hairdressingService.findAll();
		modelMap.addAttribute("hairdressings", hairdressings);
		return vista;
	}
	
	@GetMapping(path="/new")
	public String crearAppointment(ModelMap modelMap) {
		String view = "hairdressing/editHairdressing";
		modelMap.addAttribute("hairdressing", new Hairdressing());
		return view;
	}
	
	@PostMapping(path = "/save")
	public String salvarHairdressing(@Valid Hairdressing hairdressing, BindingResult result, ModelMap modelMap) {
		String view = "/hairdressing/listHairdressings";
		System.out.println("\n\n\n\n" + hairdressing + "\n\n\n\n");
		if(result.hasErrors()) {
			return "hairdressing/editHairdressing";
		}else {
			
			hairdressingService.save(hairdressing);
//			modelMap.addAttribute("message", "Appointment succesfully saved!");
//			view = listHairdressingAppointments(modelMap);
			return view;
		}
		
	}
	
	@GetMapping(path="/delete/{hairdressingId}")
	public String borrarHairdressing(@PathVariable("hairdressingId") int hairdressingId, ModelMap modelMap) {
		String view = "hairdressing/listHairdressings";
		Optional<Hairdressing> hairdressing = hairdressingService.findHairdressingById(hairdressingId);
		if(hairdressing.isPresent()) {
			hairdressingService.delete(hairdressing.get());
			modelMap.addAttribute("message", "Appointment succesfully deleted!");
		}else {
			modelMap.addAttribute("message", "Event not found!");
		}
		
		return this.listHairdressingAppointments(modelMap);
	}
	
//	@GetMapping(path="/update/{hairdressingId}")
	
}
