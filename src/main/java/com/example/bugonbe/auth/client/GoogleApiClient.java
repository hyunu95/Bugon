package com.example.bugonbe.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleApiClient {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${oauth.google.client-id}")
	private String clientId;

	@Value("${oauth.google.client-secret}")
	private String clientSecret;

	@Value("${oauth.google.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
	private static final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

	public GoogleUserInfo getUserInfo(String code) {
		String accessToken = requestAccessToken(code);
		return requestUserInfo(accessToken);
	}

	private String requestAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String requestBody = UriComponentsBuilder.newInstance()
			.queryParam("code", code)
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("grant_type", "authorization_code")
			.build().toUri().getRawQuery();

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		String response = restTemplate.postForObject(TOKEN_URI, request, String.class);
		try {
			JsonNode root = objectMapper.readTree(response);
			return root.get("access_token").asText();
		} catch (Exception e) {
			throw new RuntimeException("구글 엑세스 토큰 요청 실패", e);
		}
	}

	private GoogleUserInfo requestUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<Void> request = new HttpEntity<>(headers);

		String response = restTemplate.exchange(
			USERINFO_URI,
			HttpMethod.GET,
			request,
			String.class
		).getBody();

		try {
			JsonNode node = objectMapper.readTree(response);
			return new GoogleUserInfo(
				node.get("email").asText(),
				node.get("name").asText(),
				node.get("picture").asText(),
				node.get("id").asText()
			);
		} catch (Exception e) {
			throw new RuntimeException("구글 유저 정보 요청 실패", e);
		}
	}
}