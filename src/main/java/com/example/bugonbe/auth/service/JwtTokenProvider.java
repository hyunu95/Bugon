package com.example.bugonbe.auth.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.bugonbe.auth.config.AuthProperties;
import com.example.bugonbe.auth.domain.AccessToken;
import com.example.bugonbe.auth.domain.JwtToken;
import com.example.bugonbe.auth.domain.RefreshToken;
import com.example.bugonbe.common.exception.BugonBadRequestException;
import com.example.bugonbe.common.exception.BugonUnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class JwtTokenProvider {

	private final AuthProperties authProperties;

	public AccessToken createAccessToken(long memberId) {
		return new AccessToken(memberId, authProperties);
	}

	public RefreshToken createRefreshToken() {
		return new RefreshToken(authProperties);
	}

	public long parseAccessToken(AccessToken accessToken) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(authProperties.getAccessKey())
				.parseClaimsJws(accessToken.getValue())
				.getBody();

			return Long.parseLong(claims.getSubject());
		} catch (ExpiredJwtException exception) {
			return Long.parseLong(exception.getClaims().getSubject());
		} catch (JwtException exception) {
			throw new BugonBadRequestException(exception.getMessage());
		}
	}

	public void validate(JwtToken jwtToken) {
		if (!isUnexpired(jwtToken)) {
			throw new BugonUnauthorizedException("만료된 토큰입니다.");
		}
	}

	public boolean isUnexpired(JwtToken jwtToken) {
		try {
			Jwts.parser()
				.setSigningKey(jwtToken.getSecretKey(authProperties))
				.parseClaimsJws(jwtToken.getValue());

			return true;
		} catch (ExpiredJwtException exception) {
			return false;
		} catch (JwtException exception) {
			throw new BugonBadRequestException(exception.getMessage());
		}
	}

}
