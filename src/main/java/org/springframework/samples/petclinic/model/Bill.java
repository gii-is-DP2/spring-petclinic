
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bills")
public class Bill extends NamedEntity {

	@Column(name = "last_name")
	@NotEmpty
	protected String	lastName;

	@Column(name = "document")
	@NotEmpty
	protected String	document;

	@Column(name = "type_document")
	@NotEmpty
	protected String	typeDocument;

	@Column(name = "price")
	@NotNull
	protected Double	price;

	@Column(name = "iva")
	@NotNull
	protected Double	iva;

	@Column(name = "final_price")
	@NotNull
	protected Double	finalPrice;

	//Relations

	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt		receipt;


	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getDocument() {
		return this.document;
	}

	public void setDocument(final String document) {
		this.document = document;
	}

	public String getTypeDocument() {
		return this.typeDocument;
	}

	public void setTypeDocument(final String typeDocument) {
		this.typeDocument = typeDocument;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public Double getIva() {
		return this.iva;
	}

	public void setIva(final Double iva) {
		this.iva = iva;
	}

	public Receipt getReceipt() {
		return this.receipt;
	}

	public void setReceipt(final Receipt receipt) {
		this.receipt = receipt;
	}

	public Double getFinalPrice() {
		return this.iva / 100 * this.price + this.price;
	}
}
