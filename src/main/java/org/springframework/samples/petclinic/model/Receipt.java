
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "receipts")
public class Receipt extends BaseEntity {

	@Column(name = "price")
	@NotNull
	private Double				price;

	@Column(name = "state")
	@Enumerated
	private State				state;

	//Relations

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "appointment_id")
	private Appointment			appointment;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "receipt", fetch = FetchType.EAGER)
	private Set<Transaction>	transactions;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "receipt", fetch = FetchType.EAGER)
	private Set<Bill>			bills;


	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public State getState() {
		return this.state;
	}

	public void setState(final State state) {
		this.state = state;
	}

	public Appointment getAppointment() {
		return this.appointment;
	}

	public void setAppointment(final Appointment appointment) {
		this.appointment = appointment;
	}

	protected Set<Transaction> getTransactionsInternal() {
		if (this.transactions == null) {
			this.transactions = new HashSet<>();
		}
		return this.transactions;
	}

	public void setTransactions(final Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Transaction> getTransactions() {
		List<Transaction> sortedTransactions = new ArrayList<>(this.getTransactionsInternal());
		PropertyComparator.sort(sortedTransactions, new MutableSortDefinition("type", true, true));
		return Collections.unmodifiableList(sortedTransactions);
	}

	protected Set<Bill> getBillsInternal() {
		if (this.bills == null) {
			this.bills = new HashSet<>();
		}
		return this.bills;
	}

	public void setBills(final Set<Bill> bills) {
		this.bills = bills;
	}

	public List<Bill> getBills() {
		List<Bill> sortedBills = new ArrayList<>(this.getBillsInternal());
		PropertyComparator.sort(sortedBills, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedBills);
	}

}
