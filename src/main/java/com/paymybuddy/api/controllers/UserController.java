package com.paymybuddy.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.exceptions.EmailAlreadyExistsException;
import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.RelationAlreadyExistsException;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.dto.EmailRequestDto;
import com.paymybuddy.api.model.dto.TransferPageDto;
import com.paymybuddy.api.model.dto.UserAccountDto;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.services.user.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("/api/user/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) throws UserNotFoundException {
		UserDto user;
		user = service.findUserById(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/api/user/{id}/transfers")
	public ResponseEntity<TransferPageDto> getTransferPage(@PathVariable int id) throws UserNotFoundException {
		TransferPageDto transferPageInfo;
		transferPageInfo = service.findUserTransferPageInfo(id);
		return ResponseEntity.ok(transferPageInfo);

	}
	
	@PostMapping("/api/register")
	public ResponseEntity<UserDto> register(@RequestBody UserAccountDto accountDto) throws EmailAlreadyExistsException {
		UserDto newUser = service.registerNewUserAccount(accountDto);
		return ResponseEntity.ok(newUser);
	}
	
	@PutMapping("/api/user/{id}/add-relation")
	public ResponseEntity<UserDto> createRelation(@PathVariable int id, @RequestBody EmailRequestDto email) throws EmailNotFoundException, SelfRelationException, RelationAlreadyExistsException, UserNotFoundException  {
		UserDto user = service.addBeneficiary(id, email);
		return ResponseEntity.ok(user);
	}
}
