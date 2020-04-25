package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{
	
	@Id
	@NotEmpty
	String username;
	
	@NotEmpty
	String password;
	
	boolean enabled;
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
	@JoinTable(name = "authorities_users",
    joinColumns = { @JoinColumn(name = "user_username") },
    inverseJoinColumns = { @JoinColumn(name = "authorities_id") })
	private Set<Authorities> authorities;
	
	public List<Authorities> getAuthorities() {
		List<Authorities> authorities = new ArrayList<>(this.getAuthoritiesInternal());
		return Collections.unmodifiableList(authorities);
	}
	
	private Set<Authorities> getAuthoritiesInternal() {
		if (this.authorities == null) {
			this.authorities = new HashSet<Authorities>();
		}
		return this.authorities;
	}
	
	public void addAuthority(Authorities authority) {
		this.getAuthoritiesInternal().add(authority);
		//authority.addUser(this);
	}
	
	public boolean removeAuthority(Authorities authority) {
		return getAuthoritiesInternal().remove(authority);
	}
	
	@Override
    public int hashCode() {
        return 37;
    }
}
