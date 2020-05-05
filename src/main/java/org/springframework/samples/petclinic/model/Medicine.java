
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "medicines")
public class Medicine extends NamedEntity {

	@Column(name = "price")
	@NotNull
	private Double price;

	//Relations

	//	@ManyToMany(fetch = FetchType.EAGER)
	//	@JoinTable(name = "diagnosis_id")
	//	private Set<Diagnosis>	diagnosis;
	//
	//	@ManyToMany(fetch = FetchType.EAGER)
	//	@JoinTable(name = "desease_id")
	//	private Set<Desease>	deseases;


	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	//	protected Set<Diagnosis> getDiagnosisInternal() {
	//		if (this.diagnosis == null) {
	//			this.diagnosis = new HashSet<>();
	//		}
	//		return this.diagnosis;
	//	}
	//
	//	public void setDiagnosis(final Set<Diagnosis> diagnosis) {
	//		this.diagnosis = diagnosis;
	//	}
	//
	//	public List<Diagnosis> getDiagnosis() {
	//		List<Diagnosis> sortedDiagnosis = new ArrayList<>(this.getDiagnosisInternal());
	//		PropertyComparator.sort(sortedDiagnosis, new MutableSortDefinition("date", true, true));
	//		return Collections.unmodifiableList(sortedDiagnosis);
	//	}
	//
	//	protected Set<Desease> getDeseasesInternal() {
	//		if (this.deseases == null) {
	//			this.deseases = new HashSet<>();
	//		}
	//		return this.deseases;
	//	}
	//
	//	public void setDeseases(final Set<Desease> deseases) {
	//		this.deseases = deseases;
	//	}
	//
	//	public List<Desease> getDeseases() {
	//		List<Desease> sortedDeseases = new ArrayList<>(this.getDeseasesInternal());
	//		PropertyComparator.sort(sortedDeseases, new MutableSortDefinition("name", true, true));
	//		return Collections.unmodifiableList(sortedDeseases);
	//	}

}
