package com.example.bugonbe.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

	@PostMapping("/issue")
	public ResponseEntity<Void> issueToken(@RequestBody String refreshToken, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(false)
			.path("/")
			.maxAge(60 * 60 * 24 * 14)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteToken(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
			.httpOnly(true)
			.secure(false)
			.path("/")
			.maxAge(0)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.noContent().build();
	}
}
