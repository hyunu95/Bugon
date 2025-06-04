package com.example.bugonbe.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class BugonException extends RuntimeException {

	private final HttpStatus httpStatus;

	public BugonException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

}
