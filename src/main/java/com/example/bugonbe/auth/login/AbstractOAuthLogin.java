package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.client.OAuthUserInfo;
import com.example.bugonbe.auth.domain.AccessToken;
import com.example.bugonbe.auth.domain.RefreshToken;
import com.example.bugonbe.auth.dto.TokenResponse;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractOAuthLogin implements OAuthLogin {

	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public TokenResponse login(String code) {
		OAuthUserInfo userInfo = getUserInfo(code);
		Member member = memberService.registerOrLogin(userInfo);

		AccessToken accessToken = jwtTokenProvider.createAccessToken(member.getId());
		RefreshToken refreshToken = jwtTokenProvider.createRefreshToken();

		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);

		return new TokenResponse(accessToken.getValue(), refreshToken.getValue());
	}

	protected abstract OAuthUserInfo getUserInfo(String code);
}
