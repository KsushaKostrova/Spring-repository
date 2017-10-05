package com.kostrova.store;

import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> handleNotExistingGoodRequest(RuntimeException ex, WebRequest request) {		
		System.out.println("EXCEPTION! NOT FOUND");
		String bodyOfResponse = "No such good";
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class) 
    public void handleIllegalArgumentException() {
		System.out.println("EXCEPTION! ARGUMENTS ARE ILLEGAL");
    }
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class) 
    public void handleForbiddenException() {
		System.out.println("EXCEPTION! YOU DON'T HAVE ENOUGH RIGHTS TO SEE THIS PAGE");
    }
}