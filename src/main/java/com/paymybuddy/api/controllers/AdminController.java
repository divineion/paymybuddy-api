package com.paymybuddy.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.services.user.UserDeletionNotAllowedException;
import com.paymybuddy.api.services.user.UserNotSoftDeletedException;
import com.paymybuddy.api.services.user.UserService;

@RestController
public class AdminController {
	private final UserService service;
	
	public AdminController(UserService service) {
		this.service = service;
	}
	
	@DeleteMapping("/api/admin/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable int id) throws UserNotFoundException, UserNotSoftDeletedException, UserDeletionNotAllowedException {
		service.deleteUser(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
