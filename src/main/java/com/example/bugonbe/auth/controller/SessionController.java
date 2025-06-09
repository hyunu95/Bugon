package com.example.bugonbe.auth.controller;

import com.example.bugonbe.auth.dto.MemberInfoResponse;
import com.example.bugonbe.common.annotation.CurrentMember;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SessionController {

	private final MemberRepository memberRepository;

	@GetMapping("/me")
	public ResponseEntity<MemberInfoResponse> me(@CurrentMember Member member) {
		return ResponseEntity.ok(new MemberInfoResponse(member));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CurrentMember Member member) {
		member.updateRefreshToken(null);
		memberRepository.save(member);

		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
			.path("/")
			.httpOnly(true)
			.maxAge(0)
			.build();

		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
			.build();
	}
}
