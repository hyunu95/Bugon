package com.example.bugonbe.auth.controller;

import com.example.bugonbe.auth.dto.TokenResponse;
import com.example.bugonbe.auth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

	private final OAuthService oAuthService;

	@GetMapping("/{provider}/callback")
	public ResponseEntity<TokenResponse> oauthCallback(
		@PathVariable String provider,
		@RequestParam("code") String code,
		HttpServletResponse response
	) {
		TokenResponse tokenResponse = oAuthService.login(provider, code);

		ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.refreshToken())
			.httpOnly(true)
			.secure(false)
			.path("/")
			.maxAge(60 * 60 * 24 * 14)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok(tokenResponse);
	}
}