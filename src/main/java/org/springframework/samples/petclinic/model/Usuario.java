
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;

public class Usuario extends BaseEntity {

	// Attributes
	@Column(name = "name")
	@NotEmpty
	private String			name;

	@Column(name = "surname")
	@NotEmpty
	private String			surname;

	@Column(name = "email")
	@NotEmpty
	@Email
	private String			email;

	@Column(name = "birthdate")
	@Past
	@NotNull
	private Date			birthdate;

	@Column(name = "registrationDate")
	@Past
	@NotNull
	private Date			registrationDate;

	@Column(name = "document")
	@NotEmpty
	private String			document;

	@Column(name = "documentType")
	@NotEmpty
	private DocumentType	documentType;

	@Column(name = "avatar")
	@NotEmpty
	private String			avatar;

	@Column(name = "password")
	@NotEmpty
	@Length(min = 5, max = 60)
	private String			password;


	// Getter & Setter
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}

	public Date getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(final Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getDocument() {
		return this.document;
	}

	public void setDocument(final String document) {
		this.document = document;
	}

	public DocumentType getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(final DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(final String avatar) {
		this.avatar = avatar;
	}

	// Derived
	@Transient
	public Integer getAge() {
		Integer result = 0;

		return result;
	}
}
