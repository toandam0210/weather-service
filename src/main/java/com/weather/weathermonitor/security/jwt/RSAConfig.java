package com.weather.weathermonitor.security.jwt;

import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@Configuration
public class RSAConfig{
	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	@Value("${auth-service.url1}")
	private String url;
	@Bean
	public RSAPublicKey getPublicKey(HttpServletRequest request) throws JwkException {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		DecodedJWT jwt = JWT.decode(jwtToken);
		JwkProvider provider = new UrlJwkProvider(url);
		Jwk jwk = provider.get(jwt.getKeyId());
		RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
		return publicKey;
	}
}