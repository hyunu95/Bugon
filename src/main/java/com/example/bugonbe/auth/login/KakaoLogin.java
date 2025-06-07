package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.client.KakaoClient;
import com.example.bugonbe.auth.client.OAuthUserInfo;
import com.example.bugonbe.auth.domain.AccessToken;
import com.example.bugonbe.auth.domain.RefreshToken;
import com.example.bugonbe.auth.dto.TokenResponse;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("kakao")
@RequiredArgsConstructor
public class KakaoLogin implements OAuthLogin {

	private final KakaoClient kakaoClient;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	// 카카오 로그인 전체 처리 메서드
	@Override
	public TokenResponse login(String code) {
		// 1. 인가 코드를 카카오 서버에 보내서 사용자 정보 받기
		OAuthUserInfo userInfo = kakaoClient.getUserInfo(code);

		// 2. 해당 사용자가 이미 가입되어 있는지 확인하고, 없다면 새로 가입
		Member member = memberService.registerOrLogin(userInfo);

		// 3. JWT 엑세스 토큰과 리프레시 토큰 생성
		AccessToken accessToken = jwtTokenProvider.createAccessToken(member.getId());
		RefreshToken refreshToken = jwtTokenProvider.createRefreshToken();

		// 4. 리프레시 토큰을 DB에 저장 (멤버 엔티티에 저장하고 다시 저장)
		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);

		// 5. 최종적으로 프론트에 accessToken과 refreshToken을 전달
		return new TokenResponse(accessToken.getValue(), refreshToken.getValue());
	}
}
