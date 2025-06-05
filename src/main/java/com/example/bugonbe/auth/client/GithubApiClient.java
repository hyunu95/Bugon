package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GithubApiClient {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${oauth.github.client-id}")
	private String clientId;

	@Value("${oauth.github.client-secret}")
	private String clientSecret;

	@Value("${oauth.github.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://github.com/login/oauth/access_token";
	private static final String USERINFO_URI = "https://api.github.com/user";
	private static final String USER_EMAIL_URI = "https://api.github.com/user/emails";

	public OAuthUserInfo getUserInfo(String code) {
		String accessToken = requestAccessToken(code);
		return requestUserInfo(accessToken);
	}

	private String requestAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(MediaType.parseMediaTypes("application/json"));

		String body = UriComponentsBuilder.newInstance()
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret)
			.queryParam("code", code)
			.queryParam("redirect_uri", redirectUri)
			.build().toUri().getRawQuery();

		HttpEntity<String> request = new HttpEntity<>(body, headers);
		String response = restTemplate.postForObject(TOKEN_URI, request, String.class);

		try {
			JsonNode node = objectMapper.readTree(response);
			return node.get("access_token").asText();
		} catch (Exception e) {
			throw new RuntimeException("깃허브 엑세스 토큰 요청 실패", e);
		}
	}

	private OAuthUserInfo requestUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setAccept(MediaType.parseMediaTypes("application/json"));

		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<String> userInfoResponse = restTemplate.exchange(
			USERINFO_URI,
			org.springframework.http.HttpMethod.GET,
			entity,
			String.class
		);

		ResponseEntity<String> emailResponse = restTemplate.exchange(
			USER_EMAIL_URI,
			org.springframework.http.HttpMethod.GET,
			entity,
			String.class
		);

		try {
			JsonNode userNode = objectMapper.readTree(userInfoResponse.getBody());
			JsonNode emailNode = objectMapper.readTree(emailResponse.getBody());
			String email = emailNode.get(0).get("email").asText();

			return new OAuthUserInfo(
				email,
				userNode.get("login").asText(),
				userNode.get("avatar_url").asText(),
				userNode.get("id").asText(),
				ProviderType.GITHUB
			);
		} catch (Exception e) {
			throw new RuntimeException("깃허브 유저 정보 요청 실패", e);
		}
	}
}
