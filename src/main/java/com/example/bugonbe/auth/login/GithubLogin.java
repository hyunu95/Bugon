package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.client.GithubClient;
import com.example.bugonbe.auth.client.OAuthUserInfo;
import com.example.bugonbe.auth.service.JwtTokenProvider;
import com.example.bugonbe.member.repository.MemberRepository;
import com.example.bugonbe.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component("github")
public class GithubLogin extends AbstractOAuthLogin {

	private final GithubClient githubClient;

	public GithubLogin(
		GithubClient githubClient,
		MemberService memberService,
		MemberRepository memberRepository,
		JwtTokenProvider jwtTokenProvider
	) {
		super(memberService, memberRepository, jwtTokenProvider);
		this.githubClient = githubClient;
	}

	@Override
	protected OAuthUserInfo getUserInfo(String code) {
		return githubClient.getUserInfo(code);
	}
}
