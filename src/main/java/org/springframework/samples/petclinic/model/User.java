
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "usuarios")
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
	@NotEmpty(message = "*")
	@Past
	private Date			birthDate;

	@Column(name = "registration_date")
	@Past
	@NotEmpty(message = "*")
	private Date			registrationDate;

	@Column(name = "document")
	@NotEmpty(message = "*")
	private String			document;

	@Column(name = "document_type")
	@NotEmpty(message = "*")
	private DocumentType	documentType;

	@Column(name = "avatar")
	@NotEmpty(message = "*")
	private String			avatar;

	@Column(name = "password")
	@NotEmpty(message = "*")
	@Length(min = 5, max = 60)
	private String			password;

	@Column(name = "age")
	@Formula("(TIMESTAMPDIFF(YEAR,birth_date,CURDATE()))")
	@NotEmpty(message = "*")
	private String			age;

	@Column(name = "address")
	@NotEmpty(message = "*")
	private String			address;

	@Column(name = "city")
	@NotEmpty(message = "*")
	private String			city;

	@Column(name = "telephone")
	@NotEmpty(message = "*")
	@Digits(fraction = 0, integer = 10)
	private String			telephone;


	// Getters & Setters

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

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(final String age) {
		this.age = age;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

}
