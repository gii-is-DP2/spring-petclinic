/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdressing;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoCuidado;
import org.springframework.samples.petclinic.repository.HairdressingRepository;
import org.springframework.samples.petclinic.service.HairdressingService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class HairdressingValidator implements Validator {

	private static final String REQUIRED = "required";


	@Autowired
	HairdressingService hairdressingService;
	@Autowired
	HairdressingRepository hairdressingRepo;
	@Override
	public void validate(Object obj, Errors errors) {
		Hairdressing h = (Hairdressing) obj;
		//date
		if (h.getDate() == null){
			errors.rejectValue("date", REQUIRED, REQUIRED);
		}else if(h.getDate().isBefore(LocalDate.now().plusDays(1))) {
			errors.rejectValue("date", "The appointment has to be at least for tomorrow", "The appointment has to be at least for tomorrow");
		}
		//description
		if (h.getDescription().isEmpty()){
			errors.rejectValue("description", REQUIRED, REQUIRED);
		}if (h.getDescription().length()>20){
			errors.rejectValue("description", "Must not be longer than 20 characters", "Must not be longer than 20 characters");
		}
		//care type
		if (h.getCuidado() == null){
			errors.rejectValue("cuidado", REQUIRED, REQUIRED);
		}else if(h.getCuidado() != TipoCuidado.ESTETICA && h.getCuidado() != TipoCuidado.PELUQUERIA) {
			errors.rejectValue("cuidado", "Select a valid care type", "Select a valid care type");
		}
		System.out.println("\n\n\n\n··········Hora: " + h.getTime());
		System.out.println("\n\n\n\n··········Fecha: " + h.getDate());
		//time
		if (h.getTime() == null){
			errors.rejectValue("time", REQUIRED, REQUIRED);
		}
//		else if (hairdressingService.countHairdressingsByDateAndTime(h.getDate(), h.getTime()) != 0){
//			errors.rejectValue("time", REQUIRED + "This time isn't available, please select another", REQUIRED + "This time isn't available, please select another");
//		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Hairdressing.class.isAssignableFrom(clazz);
	}

}
