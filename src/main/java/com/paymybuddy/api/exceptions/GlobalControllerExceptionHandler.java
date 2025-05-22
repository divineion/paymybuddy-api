package com.paymybuddy.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.services.dto.ApiError;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerExceptionHandler {
	private final static Logger logger = LogManager.getLogger(GlobalControllerExceptionHandler.class);
	
	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
		ApiError apiError = new ApiError(409, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
		ApiError apiError = new ApiError(404, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<ApiError> handleEmailNotFoundException(EmailNotFoundException e) {
		ApiError apiError = new ApiError(404, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleSelfRelationException(SelfRelationException e) {
		ApiError apiError = new ApiError(400, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleRelationAlreadyExistsException(RelationAlreadyExistsException e) {
		ApiError apiError = new ApiError(409, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleInsufficientBalanceException(InsufficientBalanceException e) {
		ApiError apiError = new ApiError(400, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleInsufficientAmountException(InsufficientAmountException e) {
		ApiError apiError = new ApiError(400, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleRelationNotFoundException(RelationNotFoundException e) {
		ApiError apiError = new ApiError(400, e.getMessage());
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
}
