package com.paymybuddy.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer> {

}
