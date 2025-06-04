package com.example.bugonbe.auth.processor;

import com.example.bugonbe.auth.dto.TokenResponse;

public interface OAuthLoginProcessor {
	TokenResponse login(String code);
}
