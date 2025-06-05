package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoApiClient {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${oauth.kakao.client-id}")
	private String clientId;

	@Value("${oauth.kakao.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
	private static final String USERINFO_URI = "https://kapi.kakao.com/v2/user/me";

	public OAuthUserInfo getUserInfo(String code) {
		String accessToken = requestAccessToken(code);
		return requestUserInfo(accessToken);
	}

	private String requestAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String requestBody = UriComponentsBuilder.newInstance()
			.queryParam("grant_type", "authorization_code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("code", code)
			.build().toUri().getRawQuery();

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		String response = restTemplate.postForObject(TOKEN_URI, request, String.class);

		try {
			JsonNode root = objectMapper.readTree(response);
			return root.get("access_token").asText();
		} catch (Exception e) {
			throw new RuntimeException("카카오 엑세스 토큰 요청 실패", e);
		}
	}

	private OAuthUserInfo requestUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<Void> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
			USERINFO_URI,
			HttpMethod.GET,
			request,
			String.class
		);

		try {
			JsonNode node = objectMapper.readTree(response.getBody());
			JsonNode kakaoAccount = node.get("kakao_account");
			JsonNode profile = kakaoAccount.get("profile");

			return new OAuthUserInfo(
				kakaoAccount.get("email").asText(),
				profile.get("nickname").asText(),
				profile.get("profile_image_url").asText(),
				node.get("id").asText(),
				ProviderType.KAKAO
			);
		} catch (Exception e) {
			throw new RuntimeException("카카오 유저 정보 요청 실패", e);
		}
	}
}