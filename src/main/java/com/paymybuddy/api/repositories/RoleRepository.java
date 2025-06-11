package com.paymybuddy.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.model.RoleName;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Optional<Role> findByName(RoleName roleName);
}
