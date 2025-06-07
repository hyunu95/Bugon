package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GithubClient extends OAuthBase {

	@Value("${oauth.github.client-id}")
	private String clientId;

	@Value("${oauth.github.client-secret}")
	private String clientSecret;

	@Value("${oauth.github.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://github.com/login/oauth/access_token";
	private static final String USERINFO_URI = "https://api.github.com/user";
	private static final String USER_EMAIL_URI = "https://api.github.com/user/emails";

	public GithubClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
		super(restTemplate, objectMapper);
	}

	@Override
	public OAuthUserInfo getUserInfo(String code) {
		String accessToken = postForAccessToken(TOKEN_URI, buildBody(code));
		JsonNode userNode = getJson(USERINFO_URI, accessToken);
		JsonNode emailNode = getJson(USER_EMAIL_URI, accessToken);

		String email = emailNode.path(0).path("email").asText();

		return new OAuthUserInfo(
			email,
			userNode.path("login").asText(),
			userNode.path("avatar_url").asText(),
			userNode.path("id").asText(),
			ProviderType.GITHUB
		);
	}

	private String buildBody(String code) {
		return buildFormEncodedBody(UriComponentsBuilder.newInstance()
			.queryParam("client_id", clientId)
			.queryParam("client_secret", clientSecret)
			.queryParam("code", code)
			.queryParam("redirect_uri", redirectUri)
		);
	}
}
