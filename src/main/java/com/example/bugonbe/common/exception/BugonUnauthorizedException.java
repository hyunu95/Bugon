package com.example.bugonbe.common.exception;

import org.springframework.http.HttpStatus;

public class BugonUnauthorizedException extends BugonException {

	public BugonUnauthorizedException(String message) {
		super(message, HttpStatus.UNAUTHORIZED);
	}

}
