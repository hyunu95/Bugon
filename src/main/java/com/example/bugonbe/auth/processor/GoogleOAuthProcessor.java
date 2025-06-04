package com.example.bugonbe.auth.processor;

import org.springframework.stereotype.Component;

import com.example.bugonbe.auth.client.GoogleApiClient;
import com.example.bugonbe.auth.client.GoogleUserInfo;
import com.example.bugonbe.auth.domain.AccessToken;
import com.example.bugonbe.auth.domain.RefreshToken;
import com.example.bugonbe.auth.dto.TokenResponse;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component("google")
@RequiredArgsConstructor
public class GoogleOAuthProcessor implements OAuthLoginProcessor {

	private final GoogleApiClient googleApiClient;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public TokenResponse login(String code) {
		GoogleUserInfo userInfo = googleApiClient.getUserInfo(code);

		Member member = memberService.registerOrLogin(userInfo);

		AccessToken accessToken = jwtTokenProvider.createAccessToken(member.getId());
		RefreshToken refreshToken = jwtTokenProvider.createRefreshToken();

		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);

		return new TokenResponse(accessToken.getValue(), refreshToken.getValue());
	}
}
