package com.paymybuddy.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.exceptions.InsufficientAmountException;
import com.paymybuddy.api.exceptions.InsufficientBalanceException;
import com.paymybuddy.api.exceptions.RelationNotFoundException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.services.dto.TransferDto;
import com.paymybuddy.api.services.dto.TransferRequestDto;
import com.paymybuddy.api.services.transfer.TransferService;

@RestController
public class TransferController {
	private final TransferService service;

	public TransferController(TransferService service) {
		this.service = service;
	}

	@PostMapping("/api/transfer")
	public ResponseEntity<TransferDto> createTransfer(@RequestBody TransferRequestDto transferReqDto)
			throws UserNotFoundException, InsufficientBalanceException, InsufficientAmountException,
			RelationNotFoundException {
		TransferDto transferDto = service.createTransfer(transferReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(transferDto);
	}

}
