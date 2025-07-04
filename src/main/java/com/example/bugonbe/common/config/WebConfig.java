package com.example.bugonbe.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000", "https://bugon.vercel.app")
			.allowedMethods("GET", "POST", "PATCH", "DELETE")
			.allowCredentials(true);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
