package com.example.bugonbe.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bugonbe.auth.client.GoogleUserInfo;
import com.example.bugonbe.common.exception.BugonNotFoundException;
import com.example.bugonbe.common.exception.BugonUnauthorizedException;
import com.example.bugonbe.member.domain.AuthProvider;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.domain.ProviderType;
import com.example.bugonbe.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	public Member findById(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BugonNotFoundException("존재하지 않는 회원입니다."));

		if (member.isDeleted()) {
			throw new BugonUnauthorizedException("탈퇴한 회원입니다.");
		}

		return member;
	}

	@Transactional
	public Member registerOrLogin(GoogleUserInfo userInfo) {
		return memberRepository.findByAuthProviderProviderTypeAndAuthProviderProviderId(
				userInfo.getProviderType(),
				userInfo.getProviderId())
			.orElseGet(() -> {
				Member member = new Member(
					userInfo.getNickname(),
					userInfo.getEmail(),
					userInfo.getProfileImageUrl(),
					new AuthProvider(userInfo.getProviderType(), userInfo.getProviderId()));
				return memberRepository.save(member);
			});
	}
}
