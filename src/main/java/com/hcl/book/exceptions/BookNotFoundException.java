package com.hcl.book.exceptions;

public class BookNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	public BookNotFoundException(String message){
		this.message=message;
	}
	public String getMessage() {
		return message;
	}

}
