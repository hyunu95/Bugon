package com.example.bugonbe.common.exception;

import org.springframework.http.HttpStatus;

public class BugonBadRequestException extends BugonException {

	public BugonBadRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

}
