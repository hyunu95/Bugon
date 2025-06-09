// OAuthController.java
package com.example.bugonbe.auth.controller;

import com.example.bugonbe.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

	private final OAuthService oAuthService;

	@GetMapping("/{provider}/callback")
	public ResponseEntity<?> oauthCallback(
		@PathVariable String provider,
		@RequestParam("code") String code
	) {
		return ResponseEntity.ok(oAuthService.login(provider, code));
	}
}
