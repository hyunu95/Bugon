package com.example.bugonbe.common.exception;

import org.springframework.http.HttpStatus;

public class BugonNotFoundException extends BugonException {

	public BugonNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}

}
