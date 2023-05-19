package com.devnatao.logapi.execeptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice	
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// creating field list -> List<Exception.Field> 
		// first: Exception (class name), second Field (static class created on Exception.class)
		List<Exception.Field> fields = new ArrayList<>();
		
		// populating field list 
		for (ObjectError error: ex.getBindingResult().getAllErrors()) {
			// Obtaining field(error) name
			String fieldName = ((FieldError) error).getField();
			// getting default error message
			String errorMessage = error.getDefaultMessage();
			// adding on fields list
			fields.add(new Exception.Field(fieldName, errorMessage));
		}
		
		Exception exception = new Exception();
		exception.setStatus(status.value());
		exception.setMoment(LocalDateTime.now());
		exception.setTitle("Campos preenchidos invalidamente!");
		// adding fiels list (created on Exception.class)
		exception.setFields(fields);
		return handleExceptionInternal(ex, exception, headers, status, request);
	}
	
}