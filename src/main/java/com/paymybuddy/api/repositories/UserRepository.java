package com.paymybuddy.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
