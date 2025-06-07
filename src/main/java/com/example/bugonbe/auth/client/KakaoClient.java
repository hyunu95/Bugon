package com.example.bugonbe.auth.client;

import com.example.bugonbe.member.domain.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoClient extends OAuthBase {

	@Value("${oauth.kakao.client-id}")
	private String clientId;

	@Value("${oauth.kakao.redirect-uri}")
	private String redirectUri;

	private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
	private static final String USERINFO_URI = "https://kapi.kakao.com/v2/user/me";

	public KakaoClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
		super(restTemplate, objectMapper);
	}

	@Override
	public OAuthUserInfo getUserInfo(String code) {
		String accessToken = postForAccessToken(TOKEN_URI, buildBody(code));
		JsonNode userNode = getJson(USERINFO_URI, accessToken);

		return new OAuthUserInfo(
			userNode.path("kakao_account").path("email").asText(),
			userNode.path("properties").path("nickname").asText(),
			userNode.path("properties").path("profile_image").asText(),
			userNode.path("id").asText(),
			ProviderType.KAKAO
		);
	}

	private String buildBody(String code) {
		return buildFormEncodedBody(UriComponentsBuilder.newInstance()
			.queryParam("grant_type", "authorization_code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("code", code)
		);
	}
}