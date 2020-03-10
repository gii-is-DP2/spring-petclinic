
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

	@Column(name = "type")
	@Enumerated
	private TransactionType	type;

	@Column(name = "amount")
	@NotNull
	private Double			amount;

	@Column(name = "token")
	@NotEmpty
	private String			token;

	//Relations

	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt			receipt;


	public TransactionType getType() {
		return this.type;
	}

	public void setType(final TransactionType type) {
		this.type = type;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(final Double amount) {
		this.amount = amount;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public Receipt getReceipt() {
		return this.receipt;
	}

	public void setReceipt(final Receipt receipt) {
		this.receipt = receipt;
	}

}
