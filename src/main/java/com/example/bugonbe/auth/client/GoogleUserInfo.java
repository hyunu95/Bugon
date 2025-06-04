package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {

	private final String email;
	private final String nickname;
	private final String profileImageUrl;
	private final String providerId;

	public ProviderType getProviderType() {
		return ProviderType.GOOGLE;
	}

	public String getProviderUniqueKey() {
		return providerId;
	}
}
