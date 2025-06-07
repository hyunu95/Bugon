package com.example.bugonbe.auth.login;

import com.example.bugonbe.auth.dto.TokenResponse;

public interface OAuthLogin {
	TokenResponse login(String code);
}
