package com.paymybuddy.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.constants.LogLevel;
import com.paymybuddy.api.services.dto.ApiError;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerExceptionHandler {
	private final static Logger logger = LogManager.getLogger(GlobalControllerExceptionHandler.class);

	private ApiError logAndBuildApiError(int status, String message, Throwable e, LogLevel level) {
		switch (level) {
			case WARN -> logger.warn(e.getMessage());
			default -> logger.error(e.getMessage());
		}

		return new ApiError(status, message);
	}

	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(logAndBuildApiError(409, ApiMessages.EMAIL_ALREADY_EXISTS, e, LogLevel.ERROR));
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(logAndBuildApiError(404, ApiMessages.USER_NOT_FOUND, e, LogLevel.ERROR));
	}

	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<ApiError> handleEmailNotFoundException(EmailNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(logAndBuildApiError(404, ApiMessages.EMAIL_NOT_FOUND, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleSelfRelationException(SelfRelationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.SELF_RELATION, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleRelationAlreadyExistsException(RelationAlreadyExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(logAndBuildApiError(409, ApiMessages.RELATION_ALREADY_EXISTS, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleInsufficientBalanceException(InsufficientBalanceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.INSUFFICIENT_BALANCE, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleInsufficientAmountException(InsufficientAmountException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.INSUFFICIENT_AMOUNT, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleRelationNotFoundException(RelationNotFoundException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.RELATION_NOT_FOUND, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(logAndBuildApiError(401, ApiMessages.INVALID_CREDENTIALS, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleRoleNotFoundException(RoleNotFoundException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(logAndBuildApiError(500, ApiMessages.ROLE_NOT_FOUND, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserNotSoftDeletedException(UserNotSoftDeletedException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.USER_NOT_SOFT_DELETED, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserDeletionNotAllowedException(UserDeletionNotAllowedException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.USER_DELETION_NOT_ALLOWED, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleUserAlreadySoftDeleted(UserAlreadySoftDeleted e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(logAndBuildApiError(409, ApiMessages.USER_ALREADY_SOFT_DELETED, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleForbiddenAccessException(ForbiddenAccessException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(logAndBuildApiError(403, ApiMessages.FORBIDDEN_ACCESS, e, LogLevel.WARN));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handlePasswordMismatchException(PasswordMismatchException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.PASSWORD_MISMATCH, e, LogLevel.ERROR));
	}
	// TODO refactor

	@ExceptionHandler
	public ResponseEntity<ApiError> handleSamePasswordException(SamePasswordException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.SAME_PASSWORD, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleSameEmailException(SameEmailException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.SAME_EMAIL, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.INVALID_REQUEST_FORMAT, e, LogLevel.ERROR));
	}

	@ExceptionHandler
	public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(logAndBuildApiError(400, ApiMessages.INVALID_METHOD_ARGUMENT, e, LogLevel.ERROR));
	}
}
