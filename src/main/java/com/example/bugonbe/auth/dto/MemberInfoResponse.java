package com.example.bugonbe.auth.dto;

import com.example.bugonbe.member.domain.Member;

public record MemberInfoResponse(
	Long id,
	String nickname,
	String email,
	String profileImageUrl
) {
	public MemberInfoResponse(Member member) {
		this(
			member.getId(),
			member.getNickname(),
			member.getEmail(),
			member.getProfileImageUrl()
		);
	}
}
