package com.paymybuddy.api.services.it;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.UserAlreadySoftDeleted;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.exceptions.UserNotSoftDeletedException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.user.UserService;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	void TestSoftDeleteUserShouldPass() throws UserNotFoundException, UserAlreadySoftDeleted, EmailNotFoundException {
		User user = userService.findUserByEmail("georgia@email.com");
	    Assertions.assertNull(user.getDeletedAt());
	    
	    userService.softDeleteUser(user.getId());
	    
	    User deletedUser = userRepository.findById(user.getId()).orElseThrow();
	    		
	   Assertions.assertNotNull(deletedUser.getDeletedAt());
	}
	
	@Test
	void TestDeleteUser_ShouldThrowException() throws EmailNotFoundException {
		User user = userService.findUserByEmail("georgia@email.com");
		Assertions.assertNull(user.getDeletedAt());
			
		Assertions.assertThrows(UserNotSoftDeletedException.class, 
				() -> userService.deleteUser(user.getId()));
	}

}
