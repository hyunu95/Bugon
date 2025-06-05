package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfo {
	private final String email;
	private final String nickname;
	private final String profileImageUrl;
	private final String providerId;
	private final ProviderType providerType;

	public ProviderType getProviderType() {
		return providerType;
	}

	public String getProviderUniqueKey() {
		return providerId;
	}
}
