package com.paymybuddy.api.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByActiveEmail(String email);
	
	@Query("select ub from User u JOIN u.beneficiaries ub WHERE u.id = :id")
	List<User> findBeneficiariesById(@Param("id") int id);
	
	@Query("select count(u) > 0 ub from User u JOIN u.beneficiaries ub WHERE u.id = :currentUserId AND ub.id = :targetUserId")
	boolean beneficiaryExists(@Param("currentUserId") int currentUserId, @Param("targetUserId") int targetUserId);
	
	@Modifying
	@Query("update User u set u.balance = :balance where u.id = :id")
	void updateBalance(@Param("id") int id, @Param("balance") BigDecimal balance);
	
	@Modifying
	@Query("update User u set u.deletedAt = :date where u.id = :id")
	void softDeleteUserById(@Param("date") LocalDateTime date, @Param("id") int id);

	@Modifying
	@Query("update User u set u.password = :password where u.id = :id")
	void updatePasswordById(int id, String password);

	@Modifying
	@Query("update User u set u.email = :email where u.id = :id")
	void updateEmail(int id, String email);
}
