package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.client.GoogleClient;
import com.example.bugonbe.auth.client.OAuthUserInfo;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component("google")
public class GoogleLogin extends AbstractOAuthLogin {

	private final GoogleClient googleClient;

	public GoogleLogin(
		GoogleClient googleClient,
		MemberService memberService,
		MemberRepository memberRepository,
		JwtTokenProvider jwtTokenProvider
	) {
		super(memberService, memberRepository, jwtTokenProvider);
		this.googleClient = googleClient;
	}

	@Override
	protected OAuthUserInfo getUserInfo(String code) {
		return googleClient.getUserInfo(code);
	}
}