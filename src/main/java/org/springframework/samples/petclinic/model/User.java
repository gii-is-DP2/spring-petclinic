
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@MappedSuperclass
public class User extends BaseEntity {

	// Attributes
	@Column(name = "name")
	@NotEmpty(message = "*")
	private String			name;

	@Column(name = "surname")
	@NotEmpty(message = "*")
	private String			surname;

	@Column(name = "email")
	@NotEmpty(message = "*")
	@Email(message = "Enter a valid email address.")
	private String			email;

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
//	@NotEmpty(message = "*")
	@Past
	private Date			birthDate;

	@Column(name = "registration_date")
	@Past
//	@NotEmpty(message = "*")
	private Date			registrationDate;

	@Column(name = "document")
	@NotEmpty(message = "*")
	private String			document;

	@Column(name = "document_type")
//	@NotEmpty(message = "*")
	private DocumentType	documentType;

	@Column(name = "avatar")
//	@NotEmpty(message = "*")
	private String			avatar;

	@Column(name = "password")
	@NotEmpty(message = "*")
	@Length(min = 5, max = 60)
	private String			password;

//	@Column(name = "age")
//	@Formula("(TIMESTAMPDIFF(YEAR,birth_date,CURDATE()))")
//	@NotEmpty(message = "*")
//	private String			age;

	@Column(name = "address")
//	@NotEmpty(message = "*")
	private String			address;

	@Column(name = "city")
//	@NotEmpty(message = "*")
	private String			city;

	@Column(name = "telephone")
//	@NotEmpty(message = "*")
	@Digits(fraction = 0, integer = 10)
	private String			telephone;
	
}
