package com.paymybuddy.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.annotations.AdminOnly;
import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.exceptions.PasswordMismatchException;
import com.paymybuddy.api.exceptions.SamePasswordException;
import com.paymybuddy.api.exceptions.UserDeletionNotAllowedException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.exceptions.UserNotSoftDeletedException;
import com.paymybuddy.api.services.dto.ApiResponse;
import com.paymybuddy.api.services.dto.ChangePasswordDto;
import com.paymybuddy.api.services.user.UserService;

@RestController
public class AdminController {
	private final UserService service;
	
	public AdminController(UserService service) {
		this.service = service;
	}
	
	@AdminOnly
	@DeleteMapping("/api/admin/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable int id) throws UserNotFoundException, UserNotSoftDeletedException, UserDeletionNotAllowedException {
		service.deleteUser(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@AdminOnly
	@PutMapping("/api/admin/user/{id}/change-password")
	public ResponseEntity<ApiResponse> updatePassword(@PathVariable int id, @RequestBody ChangePasswordDto newPasswordDto) throws UserNotFoundException, PasswordMismatchException, SamePasswordException {
		service.changePasswordAdminOnly(id, newPasswordDto);
		return ResponseEntity.ok(new ApiResponse(ApiMessages.PASSWORD_SUCCESSFULLY_UPDATED));
	}
}
