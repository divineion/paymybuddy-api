package com.paymybuddy.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.constants.ApiMessages;
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
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
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
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException e) {
		ApiError apiError = new ApiError(401, ApiMessages.INVALID_CREDENTIALS);
		logger.error(ApiMessages.INVALID_CREDENTIALS);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleRoleNotFoundException(RoleNotFoundException e) {
		ApiError apiError = new ApiError(500, ApiMessages.ROLE_NOT_FOUND);
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserNotSoftDeletedException(UserNotSoftDeletedException e) {
		ApiError apiError = new ApiError(400, ApiMessages.USER_NOT_SOFT_DELETED);
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserDeletionNotAllowedException(UserDeletionNotAllowedException e) {
		ApiError apiError = new ApiError(400, ApiMessages.USER_DELETION_NOT_ALLOWED);
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserAlreadySoftDeleted(UserAlreadySoftDeleted e) {
		ApiError apiError = new ApiError(409, ApiMessages.USER_ALREADY_SOFT_DELETED);
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
}
