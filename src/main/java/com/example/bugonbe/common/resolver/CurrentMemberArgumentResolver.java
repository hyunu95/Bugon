package com.example.bugonbe.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.bugonbe.auth.service.AuthService;
import com.example.bugonbe.common.annotation.CurrentMember;
import com.example.bugonbe.common.exception.BugonException;
import com.example.bugonbe.common.exception.BugonUnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final AuthService authService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentMember.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		try {
			String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

			if (authorizationHeader == null) {
				return null;
			}

			return authService.parseAccessToken(authorizationHeader);
		} catch (BugonException exception) {
			log.warn(exception.getMessage());
			throw new BugonUnauthorizedException("액세스 토큰이 유효하지 않습니다.");
		}
	}

}
