package com.paymybuddy.api.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.annotations.AuthenticatedUser;
import com.paymybuddy.api.annotations.AuthenticatedUserOrAdmin;
import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.exceptions.EmailAlreadyExistsException;
import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.PasswordMismatchException;
import com.paymybuddy.api.exceptions.RelationAlreadyExistsException;
import com.paymybuddy.api.exceptions.RelationNotFoundException;

import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.exceptions.UserAlreadySoftDeleted;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.services.dto.ApiResponse;
import com.paymybuddy.api.services.dto.BeneficiaryDto;
import com.paymybuddy.api.services.dto.EmailRequestDto;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UpdateUserAccountDto;
import com.paymybuddy.api.services.dto.UserAccountDto;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.user.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	private final UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}

	@AuthenticatedUser
	@GetMapping("/api/user/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) throws UserNotFoundException {
		UserDto user;
		user = service.findUserById(id);
		return ResponseEntity.ok(user);
	}

	@AuthenticatedUser
	@GetMapping("/api/user/{id}/transfers")
	public ResponseEntity<TransferPageDto> getTransferPage(@PathVariable int id) throws UserNotFoundException {
		TransferPageDto transferPageInfo;
		transferPageInfo = service.findUserTransferPageInfo(id);
		return ResponseEntity.ok(transferPageInfo);

	}

	@PostMapping("/api/register")
	public ResponseEntity<UserDto> register(@Valid @RequestBody UserAccountDto accountDto) throws EmailAlreadyExistsException  {
		UserDto newUser = service.registerNewUserAccount(accountDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	@AuthenticatedUser
	@PostMapping("/api/user/{id}/add-relation")
	public ResponseEntity<BeneficiaryDto> createRelation(@PathVariable int id, @Valid @RequestBody EmailRequestDto email)
			throws EmailNotFoundException, SelfRelationException, RelationAlreadyExistsException,
			UserNotFoundException {
		BeneficiaryDto user = service.addBeneficiary(id, email);
		return ResponseEntity.ok(user);
	}
	
	@AuthenticatedUserOrAdmin
	@PutMapping("/api/user/{id}/delete-account")
	public ResponseEntity<Void> deleteAccount(@PathVariable int id) throws UserNotFoundException, UserAlreadySoftDeleted {
		service.softDeleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@AuthenticatedUser
	@DeleteMapping("api/user/{id}/remove-relation/{beneficiaryId}")
	public ResponseEntity<Void> deleteRelation(@PathVariable int id, @PathVariable int beneficiaryId)
			throws RelationNotFoundException, UserNotFoundException {
			service.removeBeneficiary(id, beneficiaryId);
			return ResponseEntity.noContent().build();
	}
	
	@AuthenticatedUser
	@PatchMapping("/api/user/{id}/change-user-info")
	public ResponseEntity<ApiResponse> updateUserInfo(@PathVariable int id, @Valid @RequestBody UpdateUserAccountDto updateAccountInfoDto) throws UserNotFoundException, PasswordMismatchException {
		service.updateUserAccount(id, updateAccountInfoDto);
		return ResponseEntity.ok(new ApiResponse(ApiMessages.USER_ACCOUNT_SUCCESSFULLY_UPDATED));
	}
}
