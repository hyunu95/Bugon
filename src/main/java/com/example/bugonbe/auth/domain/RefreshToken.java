package com.example.bugonbe.auth.domain;

import java.util.Date;

import com.example.bugonbe.auth.config.AuthProperties;
import com.example.bugonbe.common.exception.BugonBadRequestException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken implements JwtToken {

	@Column(name = "refresh_token")
	private String value;

	public RefreshToken(AuthProperties authProperties) {
		Date validity = new Date(System.currentTimeMillis() + authProperties.getRefreshExpiration());
		this.value = Jwts.builder()
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, authProperties.getRefreshKey())
			.compact();
	}

	public RefreshToken(String rawValue) {
		validate(rawValue);
		this.value = rawValue;
	}

	private void validate(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new BugonBadRequestException("잘못된 리프레시 토큰 형식입니다.");
		}
	}

	@Override
	public String getSecretKey(AuthProperties authProperties) {
		return authProperties.getRefreshKey();
	}

}
