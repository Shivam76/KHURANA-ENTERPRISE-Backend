package com.khurana.enterprise.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
		log.error("Product not found: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				"Product Not Found",
				ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
		log.error("Insufficient stock: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Insufficient Stock",
				ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
		log.error("Invalid request: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Invalid Request",
				ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage())
				.reduce((a, b) -> a + "; " + b)
				.orElse("Validation failed");
		log.error("Validation error: {}", message);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Validation Error",
				message,
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
		log.error("Missing request parameter: {}", ex.getParameterName());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Missing Parameter",
				"Required parameter '" + ex.getParameterName() + "' is missing",
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointer(NullPointerException ex) {
		log.error("Null pointer exception: ", ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				"An unexpected error occurred. Please contact support.",
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
		log.error("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
		ErrorResponse error = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				"Data Integrity Error",
				"Database constraint violation. Please check your input data.",
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {
		log.error("Database error: ", ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Database Error",
				"A database error occurred. Please try again later.",
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
		log.error("Runtime exception: ", ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		log.error("Unexpected exception: ", ex);
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				"Something went wrong. Please try again later.",
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}