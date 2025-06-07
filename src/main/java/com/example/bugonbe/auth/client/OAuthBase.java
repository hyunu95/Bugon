package com.example.bugonbe.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public abstract class OAuthBase {

	protected final RestTemplate restTemplate;
	protected final ObjectMapper objectMapper;

	protected String postForAccessToken(String uri, String requestBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(MediaType.parseMediaTypes("application/json"));

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		try {
			String response = restTemplate.postForObject(uri, request, String.class);
			return objectMapper.readTree(response).path("access_token").asText();
		} catch (Exception e) {
			throw new RuntimeException("엑세스 토큰 요청 실패", e);
		}
	}

	protected JsonNode getJson(String uri, String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setAccept(MediaType.parseMediaTypes("application/json"));

		HttpEntity<Void> entity = new HttpEntity<>(headers);

		try {
			String response = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				entity,
				String.class
			).getBody();
			return objectMapper.readTree(response);
		} catch (Exception e) {
			throw new RuntimeException("유저 정보 요청 실패", e);
		}
	}

	protected String buildFormEncodedBody(UriComponentsBuilder builder) {
		return builder.build().toUri().getRawQuery();
	}

	public abstract OAuthUserInfo getUserInfo(String code);
}
