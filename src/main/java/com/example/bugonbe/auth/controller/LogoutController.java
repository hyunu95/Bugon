package com.example.bugonbe.auth.controller;

import com.example.bugonbe.common.annotation.CurrentMember;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LogoutController {

	private final MemberRepository memberRepository;

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
