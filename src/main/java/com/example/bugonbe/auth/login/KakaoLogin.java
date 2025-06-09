package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.client.KakaoClient;
import com.example.bugonbe.auth.client.OAuthUserInfo;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component("kakao")
public class KakaoLogin extends AbstractOAuthLogin {

	private final KakaoClient kakaoClient;

	public KakaoLogin(
		KakaoClient kakaoClient,
		MemberService memberService,
		MemberRepository memberRepository,
		JwtTokenProvider jwtTokenProvider
	) {
		super(memberService, memberRepository, jwtTokenProvider);
		this.kakaoClient = kakaoClient;
	}

	@Override
	protected OAuthUserInfo getUserInfo(String code) {
		return kakaoClient.getUserInfo(code);
	}
}