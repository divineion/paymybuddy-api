package com.paymybuddy.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	int id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=60)
	RoleName name;
	
	public Role() {}

	public Role(int id, RoleName name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public RoleName getName() {
		return name;
	}
}
