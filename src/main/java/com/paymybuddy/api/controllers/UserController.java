package com.paymybuddy.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.dto.EmailRequestDto;
import com.paymybuddy.api.model.dto.TransferPageDto;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.services.user.UserService;

@RestController
public class UserController {
	@Autowired
	UserService service;
	
	@GetMapping("/api/user/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
		UserDto user;
		try {
			user = service.findUserById(id);
			return ResponseEntity.ok(user);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(404).build();
		}	
	}
	
	@GetMapping("/api/user/{id}/transfers")
	public ResponseEntity<TransferPageDto> getTransferPage(@PathVariable int id) {
		TransferPageDto transferPageInfo;
		try {
			transferPageInfo = service.findUserTransferPageInfo(id);
			return ResponseEntity.ok(transferPageInfo);
		} catch(UserNotFoundException e) {
			return ResponseEntity.status(404).build();
		}
	}
	
	@PostMapping("/api/user/{id}/add-relation")
	public ResponseEntity<UserDto> createRelation(@PathVariable int id, @RequestBody EmailRequestDto email) {
		try {
			UserDto user = service.addBeneficiary(id, email);
			return ResponseEntity.ok(user);
		} catch (EmailNotFoundException e) {
			return ResponseEntity.status(404).build();
		} catch (SelfRelationException e) {
			return ResponseEntity.status(400).build();
		}
	}
}
