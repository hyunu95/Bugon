package com.example.bugonbe.auth.domain;

import com.example.bugonbe.auth.config.AuthProperties;

public interface JwtToken {

	String getSecretKey(AuthProperties authProperties);

	String getValue();

}
