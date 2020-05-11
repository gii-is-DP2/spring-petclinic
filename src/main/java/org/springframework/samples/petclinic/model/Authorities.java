package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class Authorities extends BaseEntity {
	
	private String authority;
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "authorities")
	private Set<User> users;
	
	public List<User> getUsers() {
		List<User> users = new ArrayList<>(this.getUsersInternal());
		return Collections.unmodifiableList(users);
	}
	
	private Set<User> getUsersInternal() {
		if (this.users == null) {
			this.users = new HashSet<User>();
		}
		return this.users;
	}
	
	public void addUser(User user) {
		this.getUsersInternal().add(user);
		//user.addAuthority(this);
	}
	
	public boolean removeUser(User user) {
		return getUsersInternal().remove(user);
	}
	
	@Override
	public String toString() {
		return authority;
	}
	
	@Override
    public int hashCode() {
        return 43;
    }
}
