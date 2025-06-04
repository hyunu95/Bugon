package com.example.bugonbe.auth.dto;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {}
