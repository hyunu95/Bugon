package com.example.bugonbe.auth.service;

import com.example.bugonbe.auth.dto.TokenResponse;
import com.example.bugonbe.auth.processor.OAuthLoginProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

	private final Map<String, OAuthLoginProcessor> processorMap;

	public TokenResponse login(String provider, String code) {
		OAuthLoginProcessor processor = processorMap.get(provider.toLowerCase());
		if (processor == null) {
			throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + provider);
		}
		return processor.login(code);
	}
}
