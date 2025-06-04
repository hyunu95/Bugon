package com.example.bugonbe.auth.service;

import org.springframework.stereotype.Service;

import com.example.bugonbe.auth.domain.AccessToken;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;

	public Member parseAccessToken(String rawAccessToken) {
		AccessToken accessToken = new AccessToken(rawAccessToken);
		jwtTokenProvider.validate(accessToken);
		long memberId = jwtTokenProvider.parseAccessToken(accessToken);

		return memberService.findById(memberId);
	}

}
