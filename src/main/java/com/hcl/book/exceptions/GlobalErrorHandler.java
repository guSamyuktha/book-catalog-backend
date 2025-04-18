package com.hcl.book.exceptions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hcl.book.entities.ApiResponse;
import com.hcl.book.entities.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalErrorHandler  {
	@ExceptionHandler(BookNotFoundException.class)
	protected ApiResponse<Object> handleBookNotFoundException(BookNotFoundException ex,HttpServletRequest request) {
		 return ResponseUtil.error(ex.getMessage(), "Book not found", 404, request.getRequestURI());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpServletRequest request) {
		return ResponseUtil.error(ex.getFieldErrors()
									.stream()
									.map(error->error.getDefaultMessage())
									.toList(),			
				"Method Arguments are Invalid", 203,request.getRequestURI()	);
	}
	@ExceptionHandler(Exception.class)
	protected ApiResponse<Object> handleException(Exception ex,HttpServletRequest request) {
		return ResponseUtil.error(ex.getMessage(),"An error occurred", 203,request.getRequestURI()	);
	}
	
	
	}	