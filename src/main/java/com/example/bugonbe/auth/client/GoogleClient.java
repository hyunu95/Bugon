package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleClient extends OAuthBase {

	@Value("${oauth.google.client-id}")
	private String clientId;

	@Value("${oauth.google.client-secret}")
	private String clientSecret;

	@Value("${oauth.google.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
	private static final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

	public GoogleClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
		super(restTemplate, objectMapper);
	}

	@Override
	public OAuthUserInfo getUserInfo(String code) {
		String accessToken = postForAccessToken(TOKEN_URI, buildBody(code));
		JsonNode userNode = getJson(USERINFO_URI, accessToken);

		return new OAuthUserInfo(
			userNode.path("email").asText(),
			userNode.path("name").asText(),
			userNode.path("picture").asText(),
			userNode.path("id").asText(),
			ProviderType.GOOGLE
		);
	}

	private String buildBody(String code) {
		return buildFormEncodedBody(UriComponentsBuilder.newInstance()
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret)
			.queryParam("code", code)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("grant_type", "authorization_code")
		);
	}
}
